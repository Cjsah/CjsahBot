package net.cjsah.bot.data;

public enum CountdownMode {
    DEFAULT,
    CALENDAR,
    SECOND;

    private final String value = this.name().toLowerCase();

    public String getValue() {
        return this.value;
    }

}
