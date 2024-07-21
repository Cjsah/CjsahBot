package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DecreaseType {
    NONE(""),
    LEAVE("leave"),
    KICK("kick"),
    KICK_ME("kick_me");

    private final String type;
}
