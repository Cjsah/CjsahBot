package net.cjsah.bot.data;

public enum MemeType {
    LITTLE(1),
    BIG(2);

    private final int index;

    MemeType(int index) {
        this.index = index;
    }

    public static MemeType of(int index) {
        for (MemeType type : values()) {
            if (type.index == index) {
                return type;
            }
        }
        return null;
    }
}
