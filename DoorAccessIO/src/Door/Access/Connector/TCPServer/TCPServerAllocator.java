/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPServer;

import Door.Access.Connector.INConnector;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.NettyAllocator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负责分配TCP服务器资源，绑定本地IP和端口，查询客户端ID等操作
 *
 * @author 赖金杰
 */
public class TCPServerAllocator {

    /**
     * 线路空闲时发送 keepalive 字符串的等待时间，单位是秒
     */
    public static int IdleStateTime_Second = 90;
    
    /**
     * 线路空闲时处于空闲状态时发送的 keepalive 消息内容
     */
    public static byte[] KeepAliveMsg="KeepAlive".getBytes();
    
    
    /**
     * 存储本地绑定的服务器信息
     */
    protected ConcurrentHashMap<String, Channel> _ServerMap;

    /**
     * 存储本地客户端信息
     */
    protected ConcurrentHashMap<String, TCPServer_ClientConnector> _ClientMap;

    private ServerBootstrap TCPServerBootstrap;

    /**
     * 客戶端ID分配器
     */
    public static AtomicInteger ClientAllocator = new AtomicInteger(1);

    /**
     * 获取一个新的客户端ID号
     *
     * @return
     */
    public static int GetNewClientID() {
        return ClientAllocator.getAndIncrement();
    }

    public TCPServerAllocator(INConnectorEvent event) {
        _ServerMap = new ConcurrentHashMap<>(20);
        _ClientMap = new ConcurrentHashMap<>(20000);

        TCPServerBootstrap = new ServerBootstrap();
        TCPServerBootstrap.group(NettyAllocator.GetEventLoopGroup(), NettyAllocator.GetClientEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                //.handler(new LoggingHandler(LogLevel.INFO)) //设定日志记录
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new TCPServer_ClientChannelInitializer(event, this));
    }

    /**
     * 将客户端保存在列表中
     *
     * @param client
     */
    public void AddClient(TCPServer_ClientConnector client) {
        StringBuilder keybuf = new StringBuilder(100);

        keybuf.append("TCPServer_Client:");
        keybuf.append(client.ClientID());
        String key = keybuf.toString();

        if (!_ClientMap.containsKey(key)) {
            _ClientMap.put(key, client);
        }

        //System.out.println("Door.Access.Connector.TCPServer.TCPServerAllocator.AddClient() --" + key);
    }

    public void DeleteClient(int clientID) {
        StringBuilder keybuf = new StringBuilder(100);

        keybuf.append("TCPServer_Client:");
        keybuf.append(clientID);
        String key = keybuf.toString();
        if (_ClientMap.containsKey(key)) {
            _ClientMap.remove(key);
        }
        //System.out.println("Door.Access.Connector.TCPServer.TCPServerAllocator.DeleteClient() --" + key);
    }

    /**
     * 强制停止客户端
     */
    public void StopClient(int clientID) {
        StringBuilder keybuf = new StringBuilder(100);

        keybuf.append("TCPServer_Client:");
        keybuf.append(clientID);
        String key = keybuf.toString();
        if (_ClientMap.containsKey(key)) {
            TCPServer_ClientConnector client = _ClientMap.get(key);
            client.Close();
            _ClientMap.remove(key);
        }

    }

    public INConnector SearchClient(TCPServerClientDetail client) {
        StringBuilder keybuf = new StringBuilder(100);

        keybuf.append("TCPServer_Client:");
        keybuf.append(client.ClientID);
        String key = keybuf.toString();
        if (_ClientMap.containsKey(key)) {
            return _ClientMap.get(key);
        }
        return null;
    }

    /**
     * 将TCP服务器绑定到指定的本地IP和端口上
     *
     * @param IP
     * @param Port
     * @return
     */
    public boolean Listen(String IP, int Port) {
        StringBuilder keybuf = new StringBuilder(100);

        keybuf.append("TCPServer:");
        keybuf.append(IP);
        keybuf.append(":");
        keybuf.append(Port);
        String key = keybuf.toString();
        if (_ServerMap.containsKey(key)) {
            return true;
        }

        ChannelFuture future;
        if (IP == null) {
            IP = "0.0.0.0";
        }
        if (IP.isEmpty()) {
            IP = "0.0.0.0";
        }

        if (IP.equals("0.0.0.0")) {
            future = TCPServerBootstrap.bind(Port);
        } else {
            future = TCPServerBootstrap.bind(IP, Port);
        }
        try {
            future.sync();
            _ServerMap.put(keybuf.toString(), future.channel());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将TCP服务器绑定到默认IP的指定端口上
     *
     * @param Port
     * @return
     */
    public boolean Listen(int Port) {
        return Listen("0.0.0.0", Port);

    }

    /**
     * 停止本地TCP服务监听
     *
     * @param IP
     * @param Port
     */
    public void StopListen(String IP, int Port) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("TCPServer:");
        keybuf.append(IP);
        keybuf.append(":");
        keybuf.append(Port);
        String key = keybuf.toString();
        if (_ServerMap.containsKey(key)) {
            Channel server = _ServerMap.get(key);
            try {
                server.close().sync();
                _ServerMap.remove(key);
            } catch (Exception e) {
                System.out.println("Door.Access.Connector.TCPServer.TCPServerAllocator.StopListen()" + e.getMessage());
            }

        }
    }

    /**
     * 停止本地TCP服务监听
     *
     * @param Port
     */
    public void StopListen(int Port) {
        StopListen("0.0.0.0", Port);
    }

    public ArrayList<IPEndPoint> getServerList() {
        ArrayList<IPEndPoint> lst = new ArrayList<IPEndPoint>(20);
        if (_ServerMap == null) {
            return lst;
        }
        if (_ServerMap.size() == 0) {
            return lst;
        }
        _ServerMap.entrySet().forEach((value) -> {
            Channel ch = value.getValue();
            InetSocketAddress localAddress = (InetSocketAddress) ch.localAddress();
            String hostString = localAddress.getHostString();
            if (hostString.equals("0:0:0:0:0:0:0:0")) {
                hostString = "0.0.0.0";
            }
            IPEndPoint ip = new IPEndPoint(hostString, localAddress.getPort());
            lst.add(ip);
        });
        return lst;
    }

    public void shutdownGracefully() {
        try {
            TCPServerBootstrap = null;

        } catch (Exception e) {
        }

    }

    /**
     * 检查客户端通道的命令执行状态，推进下一个命令
     */
    public void CheckClientStatus(ExecutorService workService) {
        if (_ClientMap.size() > 0) {//异步检查
            _ClientMap.entrySet().forEach((value) -> {
                INConnector connector = value.getValue();
                workService.submit(() -> connector.run());
            });
        }
    }
}
