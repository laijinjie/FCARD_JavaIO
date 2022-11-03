/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyTCPServer;

import NettyUDPServer.UDPServerHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaifa
 */
public class NettyTCPServer_NodeClient extends SimpleChannelInboundHandler<ByteBuf> {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Logger _log;
    private String _RemoteIP;

    public NettyTCPServer_NodeClient() {
        _log = Logger.getLogger("TCP客户端处理器");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {

        int len = byteBuf.readableBytes();
        byteBuf.markReaderIndex();
        CharSequence charSequence = byteBuf.readCharSequence(len, Charset.defaultCharset());
        String readData = charSequence.subSequence(0, charSequence.length()).toString();



        _log.log(Level.INFO,
                "接收到数据,长度：" + len + ",客户端IP：" + _RemoteIP);

        byteBuf.resetReaderIndex();
        byteBuf.retain();
        //写回
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 客户端接入并准备完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        SocketAddress skt = ctx.channel().remoteAddress();
        String sIP = skt.toString();
        _RemoteIP=sIP;
        _log.log(Level.INFO, "客户端连接接入,IP：" + _RemoteIP);

        //System.out.println("Door.Access.Connector.TCPServer.TCPServer_ClientNettyHandler.channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 当连接关闭时触发此事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        _log.log(Level.INFO, "客户端断开连接,IP：" + _RemoteIP);
        //System.out.println("Door.Access.Connector.TCPServer.TCPServer_ClientNettyHandler.channelInactive()");
        super.channelInactive(ctx);
    }

}
