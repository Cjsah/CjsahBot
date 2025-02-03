package net.cjsah.bot.data.enums;

public enum HonorType {
    TALKATIVE("龙王"),
    PERFORMER("群聊之火"),
    EMOTION("快乐源泉");

    private final String name;

    HonorType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
