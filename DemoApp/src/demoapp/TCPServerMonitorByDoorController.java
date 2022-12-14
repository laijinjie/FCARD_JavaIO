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
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnector;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPServer.TCPServerAllocator;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Packet.PacketDecompileAllocator;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.Result.ReadFile_Result;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaifa
 */
public class TCPServerMonitorByDoorController implements INConnectorEvent {

    private ConnectorAllocator _Allocator;
    private String _LocalIP = null;
    private int _LocalPort = 8686;
    protected ConcurrentHashMap<String, ConnectContext> _ConnectorMap;

    protected ConcurrentHashMap<String, ConnectContext> _SNMap;

    public TCPServerMonitorByDoorController() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _ConnectorMap = new ConcurrentHashMap<String, ConnectContext>(1024);
        _SNMap = new ConcurrentHashMap<String, ConnectContext>(1024);
    }

    private void InputTestPar() {
        System.out.println("测试门禁控制板的TCP服务器：");
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("请输入本机绑定的IP：");
        _LocalIP = sc.nextLine();//接收字符串
        System.out.println("请输入本机绑定的端口号：");
        _LocalPort = sc.nextInt();

        sc.close();
    }

    public void BeginMonitor() {
        InputTestPar();

        //开始监听前，配置电脑端的 keepalive 参数
        //读空闲时间，就是多少时间没有读到消息时会发送keepalive消息进行保活，单位是秒
        //此值不能设置太小
        TCPServerAllocator.IdleStateTime_Second = 90;
        //自定义keepalive保活包消息内容
        TCPServerAllocator.KeepAliveMsg = StringUtil.HexToByte("7E30303030303030303030303030303030FFFFFFFF630B6AAC01020000000000837E");

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
        //printLog("CommandProcessEvent:" + cmd.getClass().getName());
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
        CommandDetail cmdDTL;
        String key = ConnectContext.GetConnectKey(tcpclientDTL.ClientID);
        if (_ConnectorMap.containsKey(key)) {
            ConnectContext ct = _ConnectorMap.get(key);

            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction watchEvent = (Door8800WatchTransaction) event;
                if (ct.SN == null) {
                    ct.SN = watchEvent.SN;
                    AddSN(ct);

                    BeginOpenWatch(ct.SN);
                }
            }
            printLog("WatchEvent " + event.toString());
        }

    }



    /**
     * 打开设备的实时监控功能
     *
     * @param SN
     */
    private void BeginOpenWatch(String SN) {
        CommandDetail cmdDTL;
        ConnectContext ct = GetConnectContext(SN);
        if (ct == null) {
            return;
        }

        if (ct.SN != null && ct.IsOpenWatch == false) {
            //发送开启监控命令
            cmdDTL = GetCommandDetail(ct);
            Face.System.BeginWatch cmdBeginWatch = new Face.System.BeginWatch(new CommandParameter(cmdDTL));
            ct.Connector.AddCommand(cmdBeginWatch);
            ct.IsOpenWatch = true;
        }
    }

    private CommandDetail GetCommandDetail(String SN) {
        CommandDetail result = null;

        ConnectContext ct = GetConnectContext(SN);
        if (ct != null) {
            result = GetCommandDetail(ct);
        }
        return result;
    }

    /**
     * 根据SN寻找连接上下文
     *
     * @param SN
     * @return
     */
    private ConnectContext GetConnectContext(String SN) {
        ConnectContext result = null;
        if (_SNMap.containsKey(SN)) {
            ConnectContext ct = _SNMap.get(SN);
            if (ct.IsOnline()) {
                result = ct;
            }
        }
        return result;
    }

    private CommandDetail GetCommandDetail(ConnectContext ct) {
        CommandDetail cmdDTL = new CommandDetail();
        cmdDTL.Connector = ct.ConnectorDTL;
        cmdDTL.Identity = new Door8800Identity(ct.SN, "FFFFFFFF", E_ControllerType.Door8900);
        cmdDTL.Timeout = 3000;
        cmdDTL.RestartCount = 5;
        return cmdDTL;
    }

    @Override
    public void ClientOnline(ConnectorDetail clientDTL) {
        printLog("ClientOnline " + clientDTL.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) clientDTL;

        INConnector conn = _Allocator.GetConnector(clientDTL);

        ConnectContext ct = new ConnectContext(tcpclientDTL.ClientID, conn, clientDTL);

        conn.OpenForciblyConnect();
        conn.AddWatchDecompile(clientDTL, PacketDecompileAllocator.GetDecompile(E_ControllerType.Door8900));

        //暂时加入 连接列表
        String key = "ClientID:" + tcpclientDTL.ClientID;
        _ConnectorMap.put(key, ct);
    }

    @Override
    public void ClientOffline(ConnectorDetail clientDTL) {
        printLog("ClientOffline " + clientDTL.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) clientDTL;
        String key = "ClientID:" + tcpclientDTL.ClientID;

        if (_ConnectorMap.containsKey(key)) {
            ConnectContext ct = _ConnectorMap.get(key);
            RemoveSN(ct);
            _ConnectorMap.remove(key);
        }
    }

    private void AddSN(ConnectContext ct) {
        String sKey = ct.SN;
        ConnectContext snCT;
        if (_SNMap.containsKey(ct.SN)) {
            snCT = _SNMap.get(sKey);
            snCT.SetNewContext(ct);
        } else {
            snCT = new ConnectContext(ct);
        }

        _SNMap.put(sKey, snCT);
    }

    private void RemoveSN(ConnectContext ct) {
        String sKey = ct.SN;
        ConnectContext snCT;
        if (_SNMap.containsKey(ct.SN)) {
            snCT = _SNMap.get(sKey);
            if (snCT.ClientID == ct.ClientID) {
                snCT.SetOffline();
            }
        }

    }

    private void printLog(String sLog) {
        Calendar now = Calendar.getInstance();
        String sNow = TimeUtil.FormatTime(now);
        System.out.println(sNow + " -- " + sLog);
    }
    
}
