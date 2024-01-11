package org.zy.server.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.zy.server.IMServer;
import org.zy.server.domain.model.Command;
import org.zy.server.domain.model.Result;

public class ConnectHandler {
    public static void execute(ChannelHandlerContext channelHandlerContext, Command command) {
        if (IMServer.USERS.containsKey(command.getNickname())) {
            channelHandlerContext.channel().writeAndFlush(Result.fail("该用户已上线,请换个昵称再试~"));
            // 断开连接
            channelHandlerContext.channel().disconnect();
            return;
        }

        IMServer.USERS.put(command.getNickname(), channelHandlerContext.channel());
        channelHandlerContext.channel().writeAndFlush(Result.success("与服务端建立连接成功"));
        // 返回群聊的人
        channelHandlerContext.channel().writeAndFlush(Result.success(JSON.toJSONString(IMServer.USERS.keySet())));
    }
}
