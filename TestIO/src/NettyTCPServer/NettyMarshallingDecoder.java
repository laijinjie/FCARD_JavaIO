/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NettyTCPServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class NettyMarshallingDecoder extends MarshallingDecoder{

	public NettyMarshallingDecoder(UnmarshallerProvider provider) {
		super(provider);
	}

	public NettyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize){
		super(provider, maxObjectSize);
	}
	
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return super.decode(ctx, in);
	}
	
}
