/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPServer;

import Door.Access.Connector.INConnectorEvent;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;

/**
 * 当有新客户端接入时，进行连接初始化操作
 *
 * @author 赖金杰
 */
public class TCPServer_ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    protected INConnectorEvent _Event;
    protected TCPServerAllocator _ServerAllocator;

    public TCPServer_ClientChannelInitializer(INConnectorEvent event, TCPServerAllocator alloc) {
        _Event = event;
        _ServerAllocator = alloc;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(90, 90, 0));//超时检查
        
        //创建通道处理程序
        TCPServer_ClientNettyHandler handler = new TCPServer_ClientNettyHandler();
        ch.pipeline().addLast(handler);//IO事件检查

        TCPServer_ClientConnector client = new TCPServer_ClientConnector(_ServerAllocator, _Event, ch, handler);
        handler.SetClient(client);

        //加入客户端列表，并发出客户端连接通知
        _ServerAllocator.AddClient(client);
        
    }

}
