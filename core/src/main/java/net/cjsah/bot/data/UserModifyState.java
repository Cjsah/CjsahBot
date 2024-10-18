package net.cjsah.bot.data;

public enum UserModifyState {
    JOIN(1),
    LEAVE(0);

    private final int index;

    UserModifyState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static UserModifyState of(int index) {
        return switch (index) {
            case 0 -> LEAVE;
            case 1 -> JOIN;
            default -> throw new IllegalArgumentException("Unknown UserModifyState index: " + index);
        };
    }
}
