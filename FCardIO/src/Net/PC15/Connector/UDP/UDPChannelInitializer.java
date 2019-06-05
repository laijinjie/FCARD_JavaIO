/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector.UDP;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * UDP通道初始化类
 * @author 赖金杰
 */
public class UDPChannelInitializer 
   extends ChannelInitializer<NioDatagramChannel> {

    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(20, 20, 0));//超时检查
    }
    
}
