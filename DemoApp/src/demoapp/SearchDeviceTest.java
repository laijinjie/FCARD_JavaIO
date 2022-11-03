/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.UDP.UDPConnector;
import Door.Access.Connector.UDP.UDPDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Door.Access.Door8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaifa
 */
public class SearchDeviceTest implements INConnectorEvent {

    private final Semaphore available = new Semaphore(0, true);
    private ConnectorAllocator _Allocator;
    private String _LocalIP = null;
    private int _LocalPort = 8686;
    private int _RemotePort = 8101;
    EventLoopGroup workerGroup;

    public SearchDeviceTest() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
    }

    public void syn() {
        try {
            available.acquire();
        } catch (Exception e) {
        }

    }

    private void InputTestPar() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("请输入本机绑定的IP：");
        _LocalIP = sc.nextLine();//接收字符串
        System.out.println("请输入本机绑定的端口号：");
        _LocalPort = sc.nextInt();
        System.out.println("请输入 **设备** 绑定的端口号，默认端口8101：");
        _RemotePort = sc.nextInt();
        if (_RemotePort == 0) {
            _RemotePort = 8101;
        }

        sc.close();
    }

    public void TestLibUDPServer() {
        InputTestPar();
        workerGroup = new NioEventLoopGroup();
        UDPDetail udp = new UDPDetail("255.255.255.255", _RemotePort, _LocalIP, _LocalPort);
        try {
            UDPConnector udpConn = new UDPConnector(udp);
            udpConn.SetEventHandle(this);
            udpConn.OpenForciblyConnect();  
            loopCheckStatic(udpConn);

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SearchDeviceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loopCheckStatic(UDPConnector udpConn) {
        workerGroup.schedule(()
                -> {
            udpConn.run();
            loopCheckStatic(udpConn);
        }, 10, TimeUnit.MILLISECONDS);
    }

    public void RunTest() {
        InputTestPar();
        BeginSearch();

        //syn();//阻塞进程
    }
    private int SearchNetFlag;
    private int SearchTimes;

    public void BeginSearch() {
        CommandDetail dt = new CommandDetail();

        UDPDetail udp = new UDPDetail("255.255.255.255", _RemotePort, _LocalIP, _LocalPort);

        //首先打开UDP端口绑定
        _Allocator.UDPBind(_LocalIP, _LocalPort);

        dt.Connector = udp;
        dt.Identity = new Door8800Identity("0000000000000000", Door8800Command.NULLPassword, E_ControllerType.Door8900);

        Random rnd = new Random();
        int max = 65535;
        int min = 10000;

        dt.RestartCount = 0;
        dt.Timeout = 6000;//每隔5秒发送一次，所以这里设定5秒超时

        //网络标记就是一个随机数
        SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;//网络标记

        SearchTimes = 1;//搜索次数;

        SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
        SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
        _Allocator.AddCommand(cmd);
    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("CommandCompleteEvent");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        System.out.println("CommandProcessEvent:" + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        System.out.println("ConnectorErrorEvent  INCommand");
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        System.out.println("ConnectorErrorEvent  ConnectorDetail");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        if (cmd.getClass().getName() == "Door.Access.Door8800.Command.System.SearchEquptOnNetNum") {
            SearchEquptOnNetNum searchCmd = (SearchEquptOnNetNum) cmd;
            SearchEquptOnNetNum_Result result = (SearchEquptOnNetNum_Result) searchCmd.getCommandResult();

            System.out.println("搜索设备完毕，搜索到设备数量： " + result.SearchTotal);
            for (int i = 0; i < result.SearchTotal; i++) {
                SearchEquptOnNetNum_Result.SearchResult device = result.ResultList.get(i);
                System.out.println("设备信息：SN=" + device.SN + ",IP=" + device.TCP.GetIP() + ",TCPPort=" + device.TCP.GetTCPPort());

            }

            BeginSearch();
            return;
        }
        System.out.println("CommandTimeout ");
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        System.out.println("PasswordErrorEvent ");
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        System.out.println("ChecksumErrorEvent ");
    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {
        System.out.println("WatchEvent ");
    }

    @Override
    public void ClientOnline(ConnectorDetail client) {
        System.out.println("ClientOnline " + client.toString());
    }

    @Override
    public void ClientOffline(ConnectorDetail client) {
        System.out.println("ClientOffline" + client.toString());
    }
}
