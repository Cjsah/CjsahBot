package net.cjsah.bot.data;

public enum Size {
    SMALL,
    MEDIUM,
    LARGE;

    private final String value = this.name().toLowerCase();

    public String getValue() {
        return this.value;
    }
}
