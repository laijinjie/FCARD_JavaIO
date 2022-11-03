/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.UDP;

import Door.Access.Connector.NettyAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * UDP通道的连接分配器
 *
 * @author 赖金杰
 */
public class UDPAllocator {

    private Bootstrap UDPBootstrap;

    public UDPAllocator() {

        UDPBootstrap = new Bootstrap(); //初始化客户端快速构造器
        //设定此 Bootstrap 为 UDP 数据报
        UDPBootstrap.group(NettyAllocator.GetClientEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                    }
                });
    }

    /**
     * 分配一个连接通道以用于连接到指定服务器
     *
     * @param IP
     * @param Port
     * @return
     */
    public ChannelFuture Bind(String IP, int Port) {
        return UDPBootstrap.bind(IP, Port);
    }

    public ChannelFuture Bind(int Port) {
        return UDPBootstrap.bind(Port);
    }

    public void shutdownGracefully() {
        try {
            UDPBootstrap = null;

        } catch (Exception e) {
        }

    }
}
