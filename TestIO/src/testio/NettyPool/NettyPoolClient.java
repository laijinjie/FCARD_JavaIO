/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.NettyPool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YuQi on 2017/7/31.
 */
public class NettyPoolClient {

    final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap strap = new Bootstrap();
    InetSocketAddress addr1 = new InetSocketAddress("kaifa.fcard3500.cn", 23);
    InetSocketAddress addr2 = new InetSocketAddress("10.0.0.11", 8888);

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    public void build() throws Exception {
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {

            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                System.out.println("AbstractChannelPoolMap -- " + NettyPoolClient.ThreadID());

                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), 2);
            }
        };
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        NettyPoolClient client = new NettyPoolClient();
        System.out.println("main -- " + NettyPoolClient.ThreadID());

        client.build();
        final String ECHO_REQ = "Hello Netty.$_";
        for (int i = 0; i < 10; i++) {
            // depending on when you use addr1 or addr2 you will get different pools.
            final SimpleChannelPool pool = client.poolMap.get(client.addr1);
            Future<Channel> f = pool.acquire();
            f.addListener((FutureListener<Channel>) f1 -> {
                Channel ch = f1.getNow();
                if (f1.isSuccess()) {
                    
                    ch.writeAndFlush(ECHO_REQ);
                    // Release back to pool
                    pool.release(ch);
                } else {
                    System.out.println("连接失败 -- " + NettyPoolClient.ThreadID() + "，时间：" + GetNowTime() + "\n" + "原因：" + f1.cause().getMessage());
                    //pool.release(ch);
                }
            });
        }
    }

    public static String ThreadID() {
        return "当前线程ID：" + Thread.currentThread().getId();
    }

    public static String GetNowTime() {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSSS");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
