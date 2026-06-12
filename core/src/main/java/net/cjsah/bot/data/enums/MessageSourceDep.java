package net.cjsah.bot.data.enums;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum MessageSourceDep {
    FRIEND("private", "qq"),
    GROUP("group", "group");

    private final String source;
    private final String contact;

    MessageSourceDep(String source, String contact) {
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
    public static MessageSourceDep fromName(Function<MessageSourceDep, String> key, String compare) {
        for (MessageSourceDep value : MessageSourceDep.values()) {
            if (key.apply(value).equals(compare)) {
                return value;
            }
        }
        return null;
    }
}
