package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MusicType {
    QQ("qq"),
    _163("163"),
    XM("xm"),
    CUSTOM("custom");

    private final String value;

    public static MusicType fromName(String name) {
        for (MusicType value : MusicType.values()) {
            if (value.value.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
