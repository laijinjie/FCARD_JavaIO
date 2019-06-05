/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.NettyPool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YuQi on 2017/7/31.
 */
public class NettyChannelPoolHandler implements ChannelPoolHandler {
    @Override
    public void channelReleased(Channel ch) throws Exception {
        System.out.println("通道释放." + NettyPoolClient.ThreadID() + " Channel ID: " + ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        System.out.println("通道分配." + NettyPoolClient.ThreadID() + " Channel ID: " + ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        System.out.println("通道创建 -- " + NettyPoolClient.ThreadID() + " Channel ID: " + ch.id() + "，创建时间：" + NettyPoolClient.GetNowTime() );
        SocketChannel channel = (SocketChannel) ch;
        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                .addLast(new StringDecoder()).addLast(new StringEncoder()).addLast(new NettyClientHander());

    }
    

    
    
}