package net.cjsah.bot.data.enums;

public enum FriendMsgMode {
    FRIEND("好友"),
    GROUP("临时"),
    OTHER("其他");

    private final String type;

    FriendMsgMode(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
