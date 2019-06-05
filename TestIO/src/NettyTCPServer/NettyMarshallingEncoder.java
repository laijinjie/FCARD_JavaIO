/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyTCPServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class NettyMarshallingEncoder extends MarshallingEncoder{

	public NettyMarshallingEncoder(MarshallerProvider provider) {
		super(provider);
	}

	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception{
		super.encode(ctx, msg, out);
	}
	
}
