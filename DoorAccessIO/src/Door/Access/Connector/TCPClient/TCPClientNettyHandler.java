/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPClient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author 赖金杰
 */
public class TCPClientNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private TCPClientConnector _Client;

    public TCPClientNettyHandler(TCPClientConnector client) {
        _Client = client;
    }

    public void Release() {
        _Client = null;
    }

    /**
     * 发生错误时，触发此事件
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (_Client == null) {
            return;
        }
        _Client.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (_Client == null) {
            return;
        }
        _Client.channelRead0(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (_Client != null) {
            _Client.channelActive(ctx);
        }
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
        if (_Client != null) {
            _Client.channelInactive(ctx);
        }
        super.channelInactive(ctx);
    }

    /**
     * 自定义用户事件，在这里用于接收超时事件
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (_Client != null) {
            _Client.userEventTriggered(ctx, evt);
        }
        super.userEventTriggered(ctx, evt);
    }

}
