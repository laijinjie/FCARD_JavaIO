/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author 赖金杰
 */
public class NettyAllocator {

    /**
     * 服务端事件循环组
     */
    private static EventLoopGroup NettyEventLoopGroup;
    /**
     * 客户端事件循环组
     */
    private static EventLoopGroup NettyClientEventLoopGroup;


    static {
        NettyEventLoopGroup = new NioEventLoopGroup(); //构建用于任务的事件LoopGroup
        NettyClientEventLoopGroup = new NioEventLoopGroup(); //构建用于任务的事件LoopGroup
    }
    
    

    public synchronized static EventLoopGroup GetEventLoopGroup() {
        if (NettyEventLoopGroup == null) {
            NettyEventLoopGroup = new NioEventLoopGroup();
        }
        return NettyEventLoopGroup;
    }

    public synchronized static EventLoopGroup GetClientEventLoopGroup() {
        if (NettyClientEventLoopGroup == null) {
            NettyClientEventLoopGroup = new NioEventLoopGroup();
        }
        return NettyClientEventLoopGroup;
    }

    public static void shutdownGracefully() {

        try {
            if (NettyEventLoopGroup != null) {
                NettyEventLoopGroup.shutdownGracefully().sync();
                NettyEventLoopGroup = null;
            }

            if (NettyClientEventLoopGroup != null) {
                NettyClientEventLoopGroup.shutdownGracefully().sync();
                NettyClientEventLoopGroup = null;
            }
        } catch (Exception e) {
        }

    }

}
