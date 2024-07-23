package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum MessageSource {
    FRIEND("private", "qq"),
    GROUP("group", "group");

    private final String source;
    private final String contact;

    public static MessageSource fromName(Function<MessageSource, String> function, String compare) {
        for (MessageSource value : MessageSource.values()) {
            if (function.apply(value).equals(compare)) {
                return value;
            }
        }
        return null;
    }
}
