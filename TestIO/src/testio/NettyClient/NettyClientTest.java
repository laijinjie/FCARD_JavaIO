/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.NettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 赖金杰
 */
public class NettyClientTest {

    //CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
    public static CountDownLatch latch = new CountDownLatch(1);
    private final Semaphore available = new Semaphore(0, true);
    private EventLoopGroup group;
    private Bootstrap b;
    private NettyClientHandler Handler;
    private Channel clientChannel;
    private String _inetHost;
    private int _inetPort;

    public NettyClientTest(int port, String host) {
        // 配置客户端NIO线程组
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        Handler = new NettyClientHandler(this);
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        if (ch.isOpen()) {
                            ch.pipeline().addLast(new IdleStateHandler(20, 20, 0));//超时检查
                            ch.pipeline().addLast(Handler);
                        }

                    }
                });
        _inetHost = host;
        _inetPort = port;
    }

    public NettyClientTest connect() throws Exception {
        if (clientChannel != null) {
            return this;
        }

        clientChannel = b.connect(_inetHost, _inetPort)
                .addListener(new connectCallback())
                .channel();
        return this;

    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public void Close() {
        clientChannel.close().addListener((x) -> {
            if (x.isDone()) {
                shutdown();
                release();
            }
        });
        clientChannel = null;
    }

    public void syn() {
        try {
            available.acquire();
        } catch (Exception e) {
        }

    }

    public void release() {
        available.release();
    }

    public void ReConnect() {
        if (clientChannel == null) {
            return;
        }
        EventExecutor executor = group.next();
        executor.execute(() -> {
            try {
                Thread.sleep(5000);//休眠五秒后重新连接；
                clientChannel = null;
                System.out.println("重新连接！");
                connect();
            } catch (InterruptedException ex) {
                Logger.getLogger(NettyClientTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(NettyClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    public class connectCallback implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isDone()) {
                if (future.isSuccess()) {
                    System.out.println("连接成功！");
                } else {
                    System.out.println("连接失败：" + future.cause().getMessage());
                    future.channel().close();

                    ReConnect();//5秒后重新连接；
                }
            }
            //System.out.println("success complete!!ok!!" + future.isSuccess());  
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        int port = 9000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new NettyClientTest(port, "127.0.0.1").connect();
    }

    private static String getOSSignalType() {
        return System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("win") ? "INT" : "USR2";
    }
}
