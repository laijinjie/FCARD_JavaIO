/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.NettyClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import java.util.logging.Logger;

/**
 *
 * @author 赖金杰
 */
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private volatile ScheduledFuture<?> heartBeat;
    private NettyClientTest _Client;

    private static final Logger logger = Logger
            .getLogger(NettyClientHandler.class.getName());

    private final ByteBuf firstMessage;

    public NettyClientHandler(NettyClientTest args) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        _Client = args;

    }

    private void CloseTask() {
        if (heartBeat != null) {
            System.out.println("停止定时发送");
            heartBeat.cancel(true);
            heartBeat = null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        CloseTask();
        // 释放资源
        //super.exceptionCaught(ctx, cause);

        ctx.close().addListener((f) -> {
            if (f.isDone()) {
                if (f.isSuccess()) {
                    System.out.println("连接关闭成功****");
                } else {
                    System.out.println("连接关闭失败****" + f.cause().getMessage());
                }
            }
        });

    }

    ;
    

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //System.out.println("channelRead0");
        byte[] req = new byte[msg.readableBytes()];
        msg.readBytes(req);
        String body = new String(req, "GB2312");
        //System.out.println("channelRead0 -- " + body);
        if (req[0] == 0x31 && req[1] == 0x31 && req[2] == 0x30) {
            _Client.Close();
        }
        //NettyClientTest.latch.countDown();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
        //ctx.writeAndFlush(firstMessage);
        /*heartBeat = ctx.executor().scheduleAtFixedRate(
                new HeartBeatTask(ctx), 0, 60000,
                TimeUnit.MILLISECONDS);
        System.out.println("开始定时发送");*/
    }

    private class HeartBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            byte[] req;
            try {
                req = "你好，这里是Netty客户端".getBytes("GB2312");
                ByteBuf msg = Unpooled.buffer(req.length);
                msg.writeBytes(req);
                ctx.writeAndFlush(msg);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(NettyClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * 连接已关闭
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        CloseTask();
        _Client.ReConnect();
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent state = (IdleStateEvent) evt;
            System.out.println("userEventTriggered" + state.state());
        } else {
            System.out.println("userEventTriggered -- 未知事件 " + evt.getClass().getCanonicalName());
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }

}
