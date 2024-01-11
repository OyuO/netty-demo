package org.zy.server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandType {

    CONNECTION(1001),
    CHAT(1002),
    JOIN_GROUP(1003),
    ERROR(-1);

    private final Integer code;

    public static CommandType match(Integer code) {
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ERROR;
    }
}
