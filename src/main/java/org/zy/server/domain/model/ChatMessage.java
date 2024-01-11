package org.zy.server.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMessage extends Command {

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 目标接收对象
     */
    private String target;

    /**
     * 消息内容
     */
    private String content;

}
