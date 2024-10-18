package net.cjsah.bot.data;

import org.jetbrains.annotations.Nullable;

public enum ChannelType {
    /**
     * 语音频道<p>
     * 语音聊天频道
     */
    VOICE(0),
    /**
     * 文字频道<p>
     * 仅文字聊天频道
     */
    TEXT(1),
    /**
     * 公告频道<p>
     * 公告频道仅管理员发言
     */
    ANNOUNCEMENT(2),
    /**
     * 分组类型的频道
     */
    PART_GROUP(3),
    /**
     * 临时频道<p>
     * 24小时后被删除
     */
    TEMP(4),
    /**
     * 临时频道管理器<p>
     * 点击生成临时频道
     */
    TEMP_CONTROLLER(5),
    ;

    private final int index;

    ChannelType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    @Nullable
    public static ChannelType of(int index) {
        for (ChannelType value : values()) {
            if (value.index == index) {
                return value;
            }
        }
        return null;
    }
}
