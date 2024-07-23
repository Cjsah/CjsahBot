package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageSource {
    FRIEND("private"),
    GROUP("group");

    private final String source;
}
