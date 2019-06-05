/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.NettyPool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by YuQi on 2017/7/31.
 */
public class NettyClientHander extends ChannelInboundHandlerAdapter {
    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(NettyPoolClient.ThreadID() + ",SendID:" +  count.getAndIncrement() + ":" + msg);
    }
}
