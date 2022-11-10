/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnector;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Packet.PacketDecompileAllocator;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaifa
 */
public class TCPServerMonitor implements INConnectorEvent {

    public static String GetConnectKey(int id) {
        return "ClientID:" + id;
    }

    public class ConnectContext {

        public int ClientID;
        public String MapKey;
        public INConnector Connector;
        public String SN;
        public boolean IsOpenWatch;

        public ConnectContext(int id, INConnector c) {
            ClientID = id;
            MapKey = GetConnectKey(id);
            Connector = c;
            IsOpenWatch = false;
            SN = null;
        }

    }

    private ConnectorAllocator _Allocator;
    private String _LocalIP = null;
    private int _LocalPort = 8686;
    protected ConcurrentHashMap<String, ConnectContext> _ConnectorMap;

    public TCPServerMonitor() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _ConnectorMap = new ConcurrentHashMap<String, ConnectContext>();
    }

    private void InputTestPar() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("请输入本机绑定的IP：");
        _LocalIP = sc.nextLine();//接收字符串
        System.out.println("请输入本机绑定的端口号：");
        _LocalPort = sc.nextInt();

        sc.close();
    }

    public void BeginMonitor() {
        InputTestPar();
        _Allocator.Listen(_LocalIP, _LocalPort);
        printLog("动态库的TCP服务器已启动：" + _LocalIP + ":" + _LocalPort);

    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        printLog("CommandCompleteEvent -- " + cmd.getClass().getName());
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        printLog("CommandProcessEvent:" + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        printLog("ConnectorErrorEvent  INCommand -- " + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        printLog("ConnectorErrorEvent  ConnectorDetail");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        printLog("CommandTimeout  -- " + cmd.getClass().getName());
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        printLog("PasswordErrorEvent  -- " + cmd.getClass().getName());
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        printLog("ChecksumErrorEvent  -- " + cmd.getClass().getName());
    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) detail;

        String key = GetConnectKey(tcpclientDTL.ClientID);
        if (_ConnectorMap.containsKey(key)) {
            ConnectContext ct = _ConnectorMap.get(key);

            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction watchEvent = (Door8800WatchTransaction) event;
                if (ct.SN == null) {
                    ct.SN = watchEvent.SN;
                }
            }
            if (ct.SN != null && ct.IsOpenWatch == false) {
                //发送开启监控命令
                CommandDetail cmdDTL = new CommandDetail();
                cmdDTL.Connector = detail;
                cmdDTL.Identity = new Door8800Identity(ct.SN, "FFFFFFFF", E_ControllerType.Face_Fingerprint);
                Face.System.BeginWatch cmdBeginWatch = new Face.System.BeginWatch(new CommandParameter(cmdDTL));
                ct.Connector.AddCommand(cmdBeginWatch);
                ct.IsOpenWatch = true;
                //离线消息开关
                Face.System.WriteOfflineRecordPush cmdPush = new Face.System.WriteOfflineRecordPush(new Face.System.Parameter.WriteOfflineRecordPush_Parameter(cmdDTL, true));
                ct.Connector.AddCommand(cmdPush);
            }
            printLog("WatchEvent " + event.toString());
        }

    }

    @Override
    public void ClientOnline(ConnectorDetail client) {
        printLog("ClientOnline " + client.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) client;

        INConnector conn = _Allocator.GetConnector(client);

        ConnectContext ct = new ConnectContext(tcpclientDTL.ClientID, conn);

        conn.OpenForciblyConnect();
        conn.AddWatchDecompile(client, PacketDecompileAllocator.GetDecompile(E_ControllerType.Face_Fingerprint));

        //暂时加入 连接列表
        String key = "ClientID:" + tcpclientDTL.ClientID;
        _ConnectorMap.put(key, ct);

    }

    @Override
    public void ClientOffline(ConnectorDetail client) {
        printLog("ClientOffline " + client.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) client;
        String key = "ClientID:" + tcpclientDTL.ClientID;

        if (_ConnectorMap.containsKey(key)) {
            _ConnectorMap.remove(key);
        }

    }

    private void printLog(String sLog) {
        Calendar now = Calendar.getInstance();
        String sNow = TimeUtil.FormatTime(now);
        System.out.println(sNow + " -- " + sLog);
    }
}
