package net.cjsah.bot.data;

public enum Theme {
    DEFAULT,
    PRIMARY,
    SUCCESS,
    DANGER;

    private final String value = this.name().toLowerCase();

    public String getValue() {
        return this.value;
    }
}
