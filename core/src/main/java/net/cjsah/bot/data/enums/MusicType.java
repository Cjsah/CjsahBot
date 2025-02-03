package net.cjsah.bot.data.enums;

public enum MusicType {
    QQ("qq"),
    _163("163"),
    XM("xm"),
    CUSTOM("custom");

    private final String value;

    MusicType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static MusicType fromName(String name) {
        for (MusicType value : MusicType.values()) {
            if (value.value.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
