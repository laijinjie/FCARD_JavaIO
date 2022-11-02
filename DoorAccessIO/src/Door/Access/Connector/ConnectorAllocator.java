/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandParameter;
import Door.Access.Command.INCommandResult;
import Door.Access.Command.INIdentity;
import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.TCPClient.TCPClientAllocator;
import Door.Access.Connector.TCPClient.TCPClientConnector;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.IPEndPoint;
import Door.Access.Connector.TCPServer.TCPServerAllocator;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Connector.TCPServer.TCPServer_ClientConnector;
import Door.Access.Connector.UDP.UDPAllocator;
import Door.Access.Connector.UDP.UDPDetail;
import Door.Access.Connector.UDP.UDPConnector;
import Door.Access.Data.INData;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 通讯通道任务分配器，负责调度各通讯线程和命令队列，是通讯库的主类。
 *
 * @author 赖金杰
 */
public class ConnectorAllocator {

    private static ConnectorAllocator staticConnectorAllocator;

    public static boolean IsUDPBind;

    public synchronized static ConnectorAllocator GetAllocator() {
        if (staticConnectorAllocator == null) {
            staticConnectorAllocator = new ConnectorAllocator();
        }
        return staticConnectorAllocator;
    }

    /**
     * 默认的 ByteBuf 分配器，使用池化的堆内存。
     */
    public static final ByteBufAllocator ALLOCATOR = ByteUtil.ALLOCATOR;

    protected TCPClientAllocator _TCPClientAllocator;
    protected UDPAllocator _UDPAllocator;
    protected TCPServerAllocator _ServerAllocator;

    protected ConcurrentHashMap<String, INConnector> _ConnectorMap;

    protected ExecutorService mainService, workService;

    protected INConnectorEvent _EventHeandler;
    protected ArrayList<INConnectorEvent> _EventListener;//事件回调接口

    protected ConcurrentLinkedQueue<IOEvent> _EventList;
    protected boolean _IsRelease;

    private ConnectorAllocator() {
        _ConnectorMap = new ConcurrentHashMap<>();

        _EventListener = new ArrayList<>(10);
        _EventList = new ConcurrentLinkedQueue<IOEvent>();
        workService = Executors.newFixedThreadPool(60);
        mainService = Executors.newFixedThreadPool(2);
        _EventHeandler = new ConnectorAllocatorEventCallback(this);
        _IsRelease = false;

        _TCPClientAllocator = new TCPClientAllocator();
        _UDPAllocator = new UDPAllocator();
        _ServerAllocator = new TCPServerAllocator(_EventHeandler);

        mainService.submit(() -> { //开始通讯连接器工作线程轮询
            CheckConnectorStatus();
        });
        mainService.submit(() -> { //开始事件投递
            CheckEventCallblack();
        });
    }

    /**
     * 增加事件监听者
     *
     * @param listener
     */
    public void AddListener(INConnectorEvent listener) {
        if (_IsRelease) {
            return;
        }
        synchronized (_EventListener) {
            if (!_EventListener.contains(listener)) {
                _EventListener.add(listener);
            }
        }

    }

    /**
     * 删除监听者
     *
     * @param listener
     */
    public void DeleteListener(INConnectorEvent listener) {
        if (_IsRelease) {
            return;
        }
        synchronized (_EventListener) {
            if (_EventListener.contains(listener)) {
                _EventListener.remove(listener);
            }
        }
    }

    /**
     * 释放动态库资源
     */
    public void Release() {
        if (_IsRelease) {
            return;
        }
        _IsRelease = true;

        _EventListener.clear();
        _EventListener = null;

        try {
            _ConnectorMap.forEach((k, v) -> {
                v.Release();
            });

            _ConnectorMap.clear();
            _ConnectorMap = null;

        } catch (Exception e) {
        }

        _TCPClientAllocator.shutdownGracefully();
        _TCPClientAllocator = null;

        _UDPAllocator.shutdownGracefully();
        _UDPAllocator = null;

        NettyAllocator.shutdownGracefully();

        workService.shutdownNow();
        mainService.shutdown();
        mainService.shutdownNow();

        //注销静态变量
        staticConnectorAllocator = null;
    }

    public boolean IsRelease() {
        return _IsRelease;
    }

    protected void AddEvent(IOEvent event) {
        synchronized (_EventList) {
            _EventList.offer(event);
        }

    }

    private static class IOEvent {

        public enum eEventType {
            eCommandCompleteEvent,
            eCommandProcessEvent,
            eConnectorErrorEvent,
            eCommandTimeout,
            ePasswordErrorEvent,
            eChecksumErrorEvent,
            eWatchEvent,
            eClientOnline,
            eClientOffline;
        }

        public eEventType EventType;
        public INCommand Command;
        public INCommandResult Result;
        public ConnectorDetail connectorDetail;
        public Boolean isStop;
        public INData EventData;

    }

    /**
     * 用于和内部连接器沟通的事件处理器
     */
    private class ConnectorAllocatorEventCallback implements INConnectorEvent {

        ConnectorAllocator _Allocator;

        public ConnectorAllocatorEventCallback(ConnectorAllocator allocator) {
            _Allocator = allocator;
        }

        @Override
        public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eCommandCompleteEvent;
            event.Command = cmd;
            event.Result = result;
            _Allocator.AddEvent(event);
        }

        @Override
        public void CommandProcessEvent(INCommand cmd) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eCommandProcessEvent;
            event.Command = cmd;
            _Allocator.AddEvent(event);
        }

        @Override
        public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eConnectorErrorEvent;
            event.Command = cmd;
            event.isStop = isStop;
            _Allocator.AddEvent(event);
        }

        @Override
        public void ConnectorErrorEvent(ConnectorDetail detail) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eConnectorErrorEvent;
            event.connectorDetail = detail;
            _Allocator.AddEvent(event);
        }

        @Override
        public void CommandTimeout(INCommand cmd) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eCommandTimeout;
            event.Command = cmd;
            _Allocator.AddEvent(event);
        }

        @Override
        public void PasswordErrorEvent(INCommand cmd) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.ePasswordErrorEvent;
            event.Command = cmd;
            _Allocator.AddEvent(event);
        }

        @Override
        public void ChecksumErrorEvent(INCommand cmd) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eChecksumErrorEvent;
            event.Command = cmd;
            _Allocator.AddEvent(event);
        }

        @Override
        public void WatchEvent(ConnectorDetail detail, INData eventData) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eWatchEvent;
            event.connectorDetail = detail;
            event.EventData = eventData;
            _Allocator.AddEvent(event);
        }

        @Override
        public void ClientOnline(ConnectorDetail client) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eClientOnline;
            event.connectorDetail = client;
            _Allocator.AddEvent(event);
        }

        @Override
        public void ClientOffline(ConnectorDetail client) {
            IOEvent event = new IOEvent();
            event.EventType = IOEvent.eEventType.eClientOffline;
            event.connectorDetail = client;
            _Allocator.AddEvent(event);
        }
    }

    /**
     * 将命令加入到通讯通道的队列上排队发送
     *
     * @param cmd 需要增加到队列的命令，包含通讯通道信息
     * @return true--加入成功；false--加入失败。
     */
    public synchronized boolean AddCommand(INCommand cmd) {
        if (_IsRelease) {
            return false;
        }

        if (cmd == null) {
            return false;
        }
        INCommandParameter par = cmd.getCommandParameter();
        if (par == null) {
            return false;
        }
        CommandDetail detail = par.getCommandDetail();
        if (detail == null) {
            return false;
        }
        ConnectorDetail connDetail = detail.Connector;
        if (connDetail == null) {
            return false;
        }

        INConnector connector = GetConnector(connDetail, true);
        if (connector == null) {
            return false;
        }

        connector.AddCommand(cmd);
        return true;

    }

    /**
     * 获取连接通道
     *
     * @param detail
     * @return
     */
    protected synchronized INConnector GetConnector(ConnectorDetail detail, boolean bNew) {
        switch (detail.GetConnectorType()) {
            case OnComm:
                break;
            case OnFile:
                break;
            case OnTCPClient:
                if (!(detail instanceof TCPClientDetail)) {
                    return null;
                }
                return SearchTCPClient((TCPClientDetail) detail, bNew);

            case OnTCPServer_Client:
                if (!(detail instanceof TCPServerClientDetail)) {
                    return null;
                }

                return _ServerAllocator.SearchClient((TCPServerClientDetail) detail);
            case OnUDP:
                if (!(detail instanceof UDPDetail)) {
                    return null;
                }
                return SearchUDPClient((UDPDetail) detail, bNew);
        }
        return null;
    }

    /**
     * 搜索TCP客户端的连接通道,没有就新建一个通道
     *
     * @param detail
     * @return
     */
    protected INConnector SearchTCPClient(TCPClientDetail detail, boolean bNew) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("TCPClient:");
        keybuf.append(detail.IP);
        keybuf.append(":");
        keybuf.append(detail.Port);
        String sKey = keybuf.toString();

        if (_ConnectorMap.containsKey(sKey)) {
            return _ConnectorMap.get(sKey);
        } else {
            if (bNew) {
                try {
                    TCPClientConnector Connector = new TCPClientConnector(_TCPClientAllocator, detail);
                    Connector.SetEventHandle(_EventHeandler);
                    AddConnector(sKey, Connector);
                    return _ConnectorMap.get(sKey);
                } catch (Exception ex) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public void UDPBind(String ip, int port) {
        if (IsUDPBind) {
            return;
        }
//        if(!isPortUsing(ip,port)){
//             throw new UnsupportedOperationException("UDPBind ip or port error ");
//        }
        UDPDetail detail = new UDPDetail("", 1, ip, port);
        UDPConnector connector = (UDPConnector) SearchUDPClient(detail, true);
        connector.UDPBind();
        IsUDPBind = true;
    }

    public void UDPUnBind(String ip, int port) {
        if (!IsUDPBind) {
            return;
        }
        UDPDetail detail = new UDPDetail("", 1, ip, port);
        UDPConnector connector = (UDPConnector) SearchUDPClient(detail, true);
        connector.UDPUnBind();
        IsUDPBind = false;
    }

    /**
     * 检测Ip和端口是否可用
     *
     * @param host
     * @param port
     * @return
     */
    public static boolean isPortUsing(String host, int port) {
        boolean flag = false;
        try {
            InetAddress theAddress = InetAddress.getByName(host);
            ServerSocket server = new ServerSocket(port, 10, theAddress);
            server.close();
            flag = true;
        } catch (Exception e) {

        }
        return flag;
    }

    protected INConnector SearchUDPClient(UDPDetail detail, boolean bNew) {
        if (detail.Port <= 0 || detail.LocalPort <= 0) return null;
        String sKey = detail.ToString();
        if (_ConnectorMap.containsKey(sKey)) {
            return _ConnectorMap.get(sKey);
        } else {
            if (bNew) {
                try {
                    UDPConnector Connector = new UDPConnector(_UDPAllocator, detail);
                    Connector.SetEventHandle(_EventHeandler);
                    AddConnector(sKey, Connector);
                    return _ConnectorMap.get(sKey);
                } catch (Exception ex) {
                    return null;
                }

            } else {
                return null;
            }

        }
    }

    protected void AddConnector(String key, INConnector conn) {
        synchronized (this) {
            _ConnectorMap.put(key, conn);
        }
    }

    /**
     * 检查连接器的工作状态，已推进连接器所持有的命令的进程
     */
    protected void CheckConnectorStatus() {
        ArrayList<String> arrayList = new ArrayList<>(_ConnectorMap.size());

        while (true) {
            if (_IsRelease) {
                return;
            }
            synchronized (this) {
                if (_ConnectorMap.size() > 0) {

                    _ConnectorMap.entrySet().forEach((value) -> {
                        INConnector connector = value.getValue();
                        if (!connector.TaskIsBegin()) {
                            if (!connector.IsInvalid()) {
                                switch (connector.GetStatus()) {
                                    case OnError:
                                        if (!connector.IsForciblyConnect()) {
                                            arrayList.add(value.getKey());
                                        }
                                    case OnConnectTimeout:
                                    case OnClosed:
                                    case OnConnected:
                                    case OnConnecting:
                                        connector.SetTaskIsBegin();
                                        workService.submit(()
                                                -> connector.run()); //异步检查
                                }
                            } else {
                                arrayList.add(value.getKey());
                            }
                        }

                    });

                    //需要删除的通道
                    if (arrayList.size() > 0) {
                        for (String value : arrayList) {
                            _ConnectorMap.get(value).Release();
                            _ConnectorMap.remove(value);
                        }
                        arrayList.clear();

                    }

                }
                //检查客户端的任务状态
                _ServerAllocator.CheckClientStatus(workService);
            }
            /*
            try {
                boolean wait = workService.awaitTermination(10, TimeUnit.SECONDS);  //等待所有任务完成，最大等待10分钟
                System.out.println(wait);
            } catch (Exception e) {
                System.out.println("Door.Access.Connector.ConnectorAllocator.CheckConnectorStatus()");
            }
             */
            try {

                Thread.sleep(5);
                if (_IsRelease) {
                    return;
                }
            } catch (Exception e) {
            }

        }
    }

    /**
     * 检查连接器的事件
     */
    protected void CheckEventCallblack() {

        while (true) {
            if (_IsRelease) {
                return;
            }
            while (true) {
                IOEvent event;
                synchronized (_EventList) {

                    event = _EventList.poll();
                }
                if (event != null) {
                    synchronized (_EventListener) {
                        int iLen = _EventListener.size();
                        for (int i = 0; i < iLen; i++) {
                            INConnectorEvent Listener = _EventListener.get(i);
                            try {
                                switch (event.EventType) {
                                    case eCommandCompleteEvent:
                                        Listener.CommandCompleteEvent(event.Command, event.Result);
                                        break;
                                    case eCommandProcessEvent:
                                        Listener.CommandProcessEvent(event.Command);
                                        break;
                                    case eCommandTimeout:
                                        Listener.CommandTimeout(event.Command);
                                        break;
                                    case eChecksumErrorEvent:
                                        Listener.ChecksumErrorEvent(event.Command);
                                        break;
                                    case ePasswordErrorEvent:
                                        Listener.PasswordErrorEvent(event.Command);
                                        break;
                                    case eConnectorErrorEvent:
                                        if (event.connectorDetail == null) {
                                            Listener.ConnectorErrorEvent(event.Command, event.isStop);
                                        } else {
                                            Listener.ConnectorErrorEvent(event.connectorDetail);
                                        }
                                        if (staticConnectorAllocator.IsForciblyConnect(event.connectorDetail))
                                            staticConnectorAllocator.CloseForciblyConnect(event.connectorDetail);
                                        break;
                                    case eWatchEvent:
                                        Listener.WatchEvent(event.connectorDetail, event.EventData);
                                        break;
                                    case eClientOnline:
                                        Listener.ClientOnline(event.connectorDetail);
                                        break;
                                    case eClientOffline:
                                        Listener.ClientOffline(event.connectorDetail);
                                        break;
                                }
                            } catch (Exception e) {
                                System.out.println("Door.Access.Connector.ConnectorAllocator.CheckEventCallblack() -- 发送事件时发生错误" + e.toString());
                            }

                            if (_IsRelease) {
                                return;
                            }
                        }
                    }
                } else {
                    break;
                }
                if (_IsRelease) {
                    return;
                }
            }

            try {
                Thread.sleep(50);
                if (_IsRelease) {
                    return;
                }
            } catch (Exception e) {
            }

        }

    }

    /**
     * 获取通道中的命令队列数量
     *
     * @return 命令队列数量
     */
    public int GetCommandCount(ConnectorDetail detail) {
        if (_IsRelease) {
            return 0;
        }

        if (detail == null) {
            return 0;
        }

        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return 0;
        }
        return connector.GetCommandCount();
    }

    /**
     * 判断此通道是否保持连接，即通道在发送完毕命令后保持连接
     *
     * @return true 表示通道保持打开
     */
    public boolean IsForciblyConnect(ConnectorDetail detail) {
        if (_IsRelease) {
            return false;
        }

        if (detail == null) {
            return false;
        }

        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return false;
        }
        return connector.IsForciblyConnect();
    }

    /**
     * 设定此连接器通道为保持打开状态
     */
    public void OpenForciblyConnect(ConnectorDetail detail) {
        if (_IsRelease) {
            return;
        }

        if (detail == null) {
            return;
        }

        INConnector connector = GetConnector(detail, true);
        if (connector == null) {
            return;
        }
        connector.OpenForciblyConnect();
    }

    /**
     * 禁止此连接器通道为保持连接状态，即命令发送完毕后关闭连接。
     */
    public void CloseForciblyConnect(ConnectorDetail detail) {
        if (_IsRelease) {
            return;
        }

        if (detail == null) {
            return;
        }

        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return;
        }
        connector.CloseForciblyConnect();
    }

    /**
     * 停止指定类型的命令，终止命令继续执行
     *
     * @param dtl 命令详情
     */
    public void StopCommand(ConnectorDetail detail, INIdentity dtl) {
        if (_IsRelease) {
            return;
        }

        if (detail == null) {
            return;
        }

        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return;
        }
        connector.StopCommand(dtl);

    }

    /**
     * 将TCP服务器绑定到指定的本地IP和端口上
     *
     * @param IP
     * @param Port
     * @return
     */
    public boolean Listen(String IP, int Port) {
        if (_IsRelease) {
            return false;
        }
        return _ServerAllocator.Listen(IP, Port);
    }

    /**
     * 将TCP服务器绑定到默认IP的指定端口上
     *
     * @param Port
     * @return
     */
    public boolean Listen(int Port) {
        if (_IsRelease) {
            return false;
        }
        return _ServerAllocator.Listen(Port);

    }

    /**
     * 停止本地TCP服务监听
     *
     * @param IP
     * @param Port
     */
    public void StopListen(String IP, int Port) {
        if (_IsRelease) {
            return;
        }
        _ServerAllocator.StopListen(IP, Port);
    }

    /**
     * 停止本地TCP服务监听
     *
     * @param Port
     */
    public void StopListen(int Port) {
        StopListen("0.0.0.0", Port);
    }

    /**
     * 获取本地TCP服务器列表
     *
     * @return
     */
    public ArrayList<IPEndPoint> GetTCPServerList() {
        if (_IsRelease) {
            return null;
        }

        return _ServerAllocator.getServerList();
    }

    /**
     * 给指定的通道添加数据包解析器，以便能正确解析收到的数据包
     *
     * @param detail
     * @param decompile
     */
    public boolean AddWatchDecompile(ConnectorDetail detail, INWatchResponse decompile) {
        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return false;
        }
        connector.AddWatchDecompile(detail, decompile);
        return true;
    }

    /**
     * 强制关闭通道连接
     *
     * @param detail
     * @return
     */
    public boolean CloseConnector(ConnectorDetail detail) {
        INConnector connector = GetConnector(detail, false);
        if (connector == null) {
            return false;
        }
        connector.CloseForciblyConnect();
        connector.StopCommand(null);
        if (detail.GetConnectorType() == E_ConnectorType.OnTCPServer_Client) {
            TCPServer_ClientConnector client = (TCPServer_ClientConnector) connector;
            client.Close();
        }
        return true;
    }
}
