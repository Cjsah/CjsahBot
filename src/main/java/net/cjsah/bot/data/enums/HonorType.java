package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HonorType {
    NONE(""),
    DRAGON("talkative"),
    CHAT_FIRE("performer"),
    HAPPINESS("emotion");

    private final String type;
}
