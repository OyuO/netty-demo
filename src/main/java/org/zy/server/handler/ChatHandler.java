package org.zy.server.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;
import org.zy.server.IMServer;
import org.zy.server.domain.enums.MessageType;
import org.zy.server.domain.model.ChatMessage;
import org.zy.server.domain.model.Result;

public class ChatHandler {
    public static void execute(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {
        try {
            ChatMessage chat = JSON.parseObject(textWebSocketFrame.text(), ChatMessage.class);
            switch (MessageType.match(chat.getType())) {
                case PRIVATE:
                    if (StringUtil.isNullOrEmpty(chat.getTarget())) {
                        channelHandlerContext.channel().writeAndFlush(Result.fail("消息发送失败: 消息发送前请指定接收对象"));
                        return;
                    }
                    Channel channel = IMServer.USERS.get(chat.getTarget());
                    if (null == channel || !channel.isActive()) {
                        channelHandlerContext.channel().writeAndFlush(Result.fail("消息发送失败: 对方目前不在线"));
                    } else {
                        channel.writeAndFlush(Result.success(String.format("私聊消息(%s)", chat.getNickname()), chat.getContent()));
                    }
                    break;
                case GROUP:
                    IMServer.GROUP.writeAndFlush(Result.success(String.format("群聊消息(%s)", chat.getNickname()), chat.getContent()));
                default:
                    channelHandlerContext.channel().writeAndFlush(Result.fail("不支持的消息类型"));
            }
        } catch (Exception ignored) {
        }
    }
}
