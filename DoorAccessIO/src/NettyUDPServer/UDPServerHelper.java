/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyUDPServer;

import Door.Access.Util.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaifa
 */
public class UDPServerHelper {

    private int _remotePort;
    private EventLoopGroup workerGroup;
    private Channel _UDPChannel;

    public boolean biludUDPServer(String sLocalIP, int iPort, ChannelHandler udpHandler) {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap UDPBootstrap = new Bootstrap();
            UDPBootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            _UDPChannel = ch;
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(udpHandler);

                        }
                    });

            ChannelFuture future;
            if (sLocalIP.isEmpty()) {
                future = UDPBootstrap.bind(iPort).sync();
                System.out.println("已绑定UDP：" + iPort);
            } else {
                future = UDPBootstrap.bind(sLocalIP, iPort).sync();
                System.out.println("已绑定UDP：" + sLocalIP + ":" + iPort);
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(UDPServerHelper.class.getName()).log(Level.SEVERE, null, ex);
            workerGroup.shutdownGracefully();
            return false;
        }
    }

    public Channel getUDPChannel() {
        return _UDPChannel;
    }
    
    public EventLoopGroup getEventLoop() {
        return workerGroup;
    }

    public void RunTest(String sLocalIP, int iPort, int remotePort) {
        _remotePort = remotePort;
        boolean ret = biludUDPServer(sLocalIP, iPort, new NettyUDPServerHandler());
        if (ret) {
            SendPacketTask sendpk = new SendPacketTask(_UDPChannel);
            workerGroup.schedule(sendpk, 5, TimeUnit.SECONDS);
        }

    }

    public class SendPacketTask implements Runnable {

        private Channel _writeChannel;

        public SendPacketTask(Channel input) {
            _writeChannel = input;
        }

        @Override
        public void run() {
            _writeChannel.eventLoop().schedule(this, 5, TimeUnit.SECONDS);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("255.255.255.255", _remotePort);

            ByteBuf byteBuf = _writeChannel.alloc().buffer(50);
//            String udpMsg = "UDP广播消息";
//            byteBuf.writeBytes(udpMsg.getBytes(Charset.defaultCharset()));

            String sHex = "7E30303030303030303030303030303030FFFFFFFFBFBFAABB01FE0000000002401D3D7E";
            StringUtil.HextoByteBuf(sHex, byteBuf);
            DatagramPacket sendpk = new DatagramPacket(byteBuf, inetSocketAddress);
            _writeChannel.writeAndFlush(sendpk);
            System.out.println("发送广播测试包....");
        }

    }
}
