package org.zy.server.domain.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    //私聊
    PRIVATE(1),
    //群聊
    GROUP(2),
    //不支持的类型
    ERROR(-1);

    private final Integer type;

    public static MessageType match(Integer type) {
        for (MessageType value : MessageType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return ERROR;
    }
}
