package net.cjsah.bot.data;

public enum TextType {
    TEXT("plain-text"),
    MARKDOWN("markdown");

    private final String value;

    TextType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
