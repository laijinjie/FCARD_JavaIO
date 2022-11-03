/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyUDPServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author kaifa
 */
public class NettyUDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket data) throws Exception {
        ByteBuf byteBuf = data.content();
        int len = byteBuf.readableBytes();
        byteBuf.markReaderIndex();
        CharSequence charSequence = byteBuf.readCharSequence(len, Charset.defaultCharset());
        String readData = charSequence.subSequence(0, charSequence.length()).toString();

        InetSocketAddress skt = data.sender();
        int remotePort = skt.getPort();
        String sIP = skt.getAddress().getHostAddress();


        System.out.println("接收到数据：" + readData + ",客户端IP：" + sIP + ":" + remotePort);
        
        byteBuf.resetReaderIndex();
        byteBuf.retain();
        InetSocketAddress writeRemote = new InetSocketAddress(sIP, remotePort);

        DatagramPacket dp = new DatagramPacket(byteBuf, writeRemote);
        channelHandlerContext.writeAndFlush(dp);
    }

}
