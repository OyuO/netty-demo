package org.zy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.zy.server.handler.ConnectionBrokenHandler;
import org.zy.server.handler.WebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IMServer {

    public static final Map<String, Channel> USERS = new ConcurrentHashMap<>(1024);

    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new HttpServerCodec())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(1024 * 64))
                                .addLast(new WebSocketServerProtocolHandler("/"))
                                .addLast(new WebSocketHandler())
                                .addLast(new ConnectionBrokenHandler());
                    }
                });

        try {
            bootstrap.bind(8888).sync();
            System.out.println("Server启动成功!");
        } catch (InterruptedException e) {
            System.out.println("Server启动失败!");
        }

    }
}
