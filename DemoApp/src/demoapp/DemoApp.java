/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Connector.UDP.UDPConnector;
import Door.Access.Connector.UDP.UDPDetail;
import NettyTCPServer.NettyTCPServerTest;
import NettyUDPServer.UDPServerHelper;
import java.util.concurrent.Semaphore;

/**
 *
 * @author kaifa
 */
public class DemoApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        //TestSearchDevice();
        //TestUDPServer();
        //TestLibUDP();
        //TestTCPServer();
        TestLibTCPServer();

        Semaphore available = new Semaphore(0, true);
        available.acquire();
    }

    public static void TestTCPServer() {
        NettyTCPServerTest tcpserver = new NettyTCPServerTest();
        System.out.println("准备运行TCP服务器");

        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("请输入本机绑定的IP：");
        String sLocalIP = sc.nextLine();//接收字符串
        System.out.println("请输入本机绑定的端口号：");
        int iLocalPort = sc.nextInt();
        tcpserver.bind(sLocalIP, iLocalPort);
    }

    public static void TestLibTCPServer() {
        TCPServerMonitor tcpserver = new TCPServerMonitor();
        System.out.println("准备运行动态库的TCP服务器");
        tcpserver.BeginMonitor();
    }

    public static void TestUDPServer() {
        System.out.println("准备运行UDP服务器");

        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("请输入本机绑定的IP：");
        String sLocalIP = sc.nextLine();//接收字符串
        System.out.println("请输入本机绑定的端口号：");
        int iLocalPort = sc.nextInt();
        System.out.println("请输入 自动发送的广播目标端口号：");
        int iRemotePort = sc.nextInt();
        if (iRemotePort == 0) {
            iRemotePort = 8101;
        }

        // TODO code application logic here
        UDPServerHelper test = new UDPServerHelper();

        test.RunTest(sLocalIP, iLocalPort, iRemotePort);
    }

    public static void TestSearchDevice() {
        System.out.println("准备测试搜索设备");
        // TODO code application logic here
        SearchDeviceTest test = new SearchDeviceTest();

        test.RunTest();
    }

    public static void TestLibUDP() {
        System.out.println("准备测试动态库UDP");
        // TODO code application logic here
        SearchDeviceTest test = new SearchDeviceTest();

        test.TestLibUDPServer();
    }

}
