/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector.TCPServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 通道处理协助类
 * 通道触发顺序：
 * channelActive() 连接接入
 * channelRead0() 数据到达
 * channelInactive() 连接关闭
 * @author Administrator
 */
public class TCPServer_ClientNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private TCPServer_ClientConnector _Client;

    
    public void SetClient(TCPServer_ClientConnector client)
    {
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
        //System.out.println("Net.PC15.Connector.TCPServer.TCPServer_ClientNettyHandler.exceptionCaught()");
        _Client.exceptionCaught(ctx, cause);
    }

    /**
     * 接收到客户端数据包
     * @param ctx
     * @param msg
     * @throws Exception 
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (_Client == null) {
            return;
        }
        //System.out.println("Net.PC15.Connector.TCPServer.TCPServer_ClientNettyHandler.channelRead0()");
        
        _Client.channelRead0(ctx, msg);
    }

    /**
     * 客户端接入并准备完毕
     * @param ctx
     * @throws Exception 
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (_Client != null) {
            _Client.channelActive(ctx);
        }
        //System.out.println("Net.PC15.Connector.TCPServer.TCPServer_ClientNettyHandler.channelActive()");
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
        //System.out.println("Net.PC15.Connector.TCPServer.TCPServer_ClientNettyHandler.channelInactive()");
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
        //System.out.println("Net.PC15.Connector.TCPServer.TCPServer_ClientNettyHandler.userEventTriggered()");
        super.userEventTriggered(ctx, evt);
    }
    
}
