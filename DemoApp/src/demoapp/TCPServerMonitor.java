/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnector;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Packet.PacketDecompileAllocator;
import Door.Access.Util.TimeUtil;
import java.util.Calendar;

/**
 *
 * @author kaifa
 */
public class TCPServerMonitor implements INConnectorEvent {

    private ConnectorAllocator _Allocator;
    private String _LocalIP = null;
    private int _LocalPort = 8686;

    public TCPServerMonitor() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
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
        printLog("CommandCompleteEvent");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        printLog("CommandProcessEvent:" + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        printLog("ConnectorErrorEvent  INCommand");
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        printLog("ConnectorErrorEvent  ConnectorDetail");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        printLog("CommandTimeout ");
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        printLog("PasswordErrorEvent ");
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        printLog("ChecksumErrorEvent ");
    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {
        printLog("WatchEvent " + event.toString());
    }

    @Override
    public void ClientOnline(ConnectorDetail client) {
        printLog("ClientOnline " + client.toString());
        INConnector conn = _Allocator.GetConnector(client);
        conn.OpenForciblyConnect();
        conn.AddWatchDecompile(client, PacketDecompileAllocator.GetDecompile(E_ControllerType.Face_Fingerprint));
    }

    @Override
    public void ClientOffline(ConnectorDetail client) {
        printLog("ClientOffline " + client.toString());
    }
    
    private void printLog(String sLog)
    {
        Calendar now = Calendar.getInstance();
        String sNow=TimeUtil.FormatTime(now);
        System.out.println(sNow + " -- " + sLog);
    }
}
