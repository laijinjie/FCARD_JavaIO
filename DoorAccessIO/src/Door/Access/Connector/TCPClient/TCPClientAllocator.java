/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPClient;

import Door.Access.Connector.NettyAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty网络通道分配器
 *
 * @author 赖金杰
 */
public class TCPClientAllocator {

    public static final int CONNECT_TIMEOUT_MILLIS_MAX = 30000;//最大30秒连接超时
    public static final int CONNECT_TIMEOUT_MILLIS_MIN = 1000;//最小1秒连接超时
    public static final int CONNECT_RECONNECT_MAX = 5;//最大重新连接次数
    
    private Bootstrap TCPBootstrap;
    private TCPClientChannelInitializer TCPInitializer;

    public TCPClientAllocator() {
        
        TCPBootstrap = new Bootstrap(); //初始化客户端快速构造器
        TCPInitializer = new TCPClientChannelInitializer();//初始化通道初始化工具
        //设定此 Bootstrap 为 TCP Client 
        TCPBootstrap.group(NettyAllocator.GetClientEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS_MAX)//最大30秒连接超时
                .handler(TCPInitializer);
    }

    /**
     * 分配一个连接通道以用于连接到指定服务器
     *
     * @param IP
     * @param Port
     * @param timeout 单位毫秒
     * @return
     */
    public ChannelFuture connect(String IP, int Port,int timeout) {
        if (TCPInitializer == null) {
            return null;
        }
        
        TCPBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);//最大30秒连接超时
        return TCPBootstrap.connect(IP, Port);
    }

    public void shutdownGracefully() {
        TCPInitializer = null;
        try {
            TCPBootstrap = null;
            
        } catch (Exception e) {
        }

    }
}
