/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyTCPServer;

import NettyUDPServer.UDPServerHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaifa
 */
public class NettyTCPServerTest {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void bind(String sLocalIP, int iPort) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(sLocalIP, iPort).sync();
            
            Logger.getLogger(UDPServerHelper.class.getName()).log(Level.INFO, 
                    "Netty TCP Server 服务已启动： 监听地址： " + sLocalIP + ":" + iPort);
            
            f.channel().closeFuture().sync();
        } catch (Exception ex) {
            Logger.getLogger(UDPServerHelper.class.getName()).log(Level.SEVERE, null, ex);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChildChannelHandler extends
            ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyTCPServer_NodeClient());
        }

    }

}
