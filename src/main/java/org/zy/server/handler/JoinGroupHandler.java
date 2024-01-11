package org.zy.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.zy.server.IMServer;
import org.zy.server.domain.model.Result;

public class JoinGroupHandler {
    public static void execute(ChannelHandlerContext channelHandlerContext) {
        //将Channel添加到ChannelGroup
        IMServer.GROUP.add(channelHandlerContext.channel());
        channelHandlerContext.channel().writeAndFlush(Result.success("加入系统默认群聊成功~"));
    }

}
