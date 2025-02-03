package net.cjsah.bot.data.enums;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum MessageSource {
    FRIEND("private", "qq"),
    GROUP("group", "group");

    private final String source;
    private final String contact;

    MessageSource(String source, String contact) {
        this.source = source;
        this.contact = contact;
    }

    public String getSource() {
        return this.source;
    }

    public String getContact() {
        return this.contact;
    }

    @Nullable
    public static MessageSource fromName(Function<MessageSource, String> key, String compare) {
        for (MessageSource value : MessageSource.values()) {
            if (key.apply(value).equals(compare)) {
                return value;
            }
        }
        return null;
    }
}
