package org.zy.server.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.zy.server.domain.enums.CommandType;
import org.zy.server.domain.model.Command;
import org.zy.server.domain.model.Result;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        try {
            Command command = JSON.parseObject(textWebSocketFrame.text(), Command.class);
            switch (CommandType.match(command.getCode())) {
                case CONNECTION:
                    ConnectHandler.execute(channelHandlerContext, command);
                    break;
                case CHAT:
                    ChatHandler.execute(channelHandlerContext, textWebSocketFrame);
                    break;
                case JOIN_GROUP:
                    JoinGroupHandler.execute(channelHandlerContext);
                    break;
                default:
                    channelHandlerContext.channel().writeAndFlush(Result.fail("不支持该CODE"));
            }
        } catch (Exception e) {
            channelHandlerContext.channel().writeAndFlush(Result.fail("错误消息: " + e.getMessage()));
        }
    }
}
