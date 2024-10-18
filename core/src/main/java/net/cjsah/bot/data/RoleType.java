package net.cjsah.bot.data;

public enum RoleType {
    DEFAULT(0),
    GAME(1),
    BOT(2),
    USER_ADMIN(3),
    TEXT_CHANNEL_ADMIN(4),
    VOICE_CHANNEL_ADMIN(5),
    COMMUNITY_BUILDER(6),
    ADVANCE_ADMIN(7),
    VISITOR(254),
    ALL(255),
    ;

    private final int index;

    RoleType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static RoleType of(int index) {
        for (RoleType type : values()) {
            if (type.index == index) {
                return type;
            }
        }
        return DEFAULT;
    }
}
