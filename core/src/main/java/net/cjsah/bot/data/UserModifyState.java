package net.cjsah.bot.data;

public enum UserModifyState {
    IN(1),
    OUT(0);

    private final int index;

    UserModifyState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static UserModifyState of(int index) {
        return switch (index) {
            case 0 -> OUT;
            case 1 -> IN;
            default -> throw new IllegalArgumentException("Unknown UserModifyState index: " + index);
        };
    }
}
