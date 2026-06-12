package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;

public enum FriendMsgMode {
    FRIEND("好友"),
    GROUP("临时"),
    OTHER("其他");

    public static final Codec<FriendMsgMode> CODEC = Codec.STRING.xmap(
        s -> switch (s) {
            case "好友" -> FRIEND;
            case "临时" -> GROUP;
            default -> OTHER;
        },
        FriendMsgMode::getType
    );

    private final String type;

    FriendMsgMode(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
