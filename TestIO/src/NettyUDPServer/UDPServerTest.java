/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyUDPServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaifa
 */
public class UDPServerHelper {

    private final Semaphore available = new Semaphore(0, true);
    private ExecutorService workService;

    public void syn() {
        try {
            available.acquire();
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {
        UDPServerHelper test = new UDPServerHelper();

        test.RunTest();

    }

    public void RunTest() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap UDPBootstrap = new Bootstrap();
            UDPBootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new NettyUDPServerHandler());

                        }
                    });
            int udpLocalPort = 1889;
            String localIp = "192.168.1.24";
            ChannelFuture future = UDPBootstrap.bind(localIp, udpLocalPort).sync();
            System.out.println("已绑定UDP：" + localIp + ":" + udpLocalPort);
            syn();
//            future.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            Logger.getLogger(UDPServerHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
