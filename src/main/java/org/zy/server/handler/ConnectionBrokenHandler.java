package org.zy.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.zy.server.IMServer;

import java.util.Map;

public class ConnectionBrokenHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 客户端断开连接时，删除对应channel
        Channel channel = ctx.channel();
        for (Map.Entry<String, Channel> entry : IMServer.USERS.entrySet()) {
            if (channel.equals(entry.getValue())) {
                IMServer.USERS.remove(entry.getKey());
            }
        }
    }
}
