package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.Event;

public class MessageEmojiEvent extends Event {
    private final String channelId;
    private final String msgId;
    private final int userId;
    private final String emoji;
    private final boolean append;

    public MessageEmojiEvent(JSONObject json, boolean append) {
        this.channelId = json.getString("channel_id");
        this.msgId = json.getString("msg_id");
        this.userId = json.getIntValue("user_id");
        this.emoji = json.getString("emoji");
        this.append = append;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public boolean isAppend() {
        return this.append;
    }

    public static MessageEmojiEvent create(JSONObject json) {
        if (json.getIntValue("is_add") == 1) {
            return new MessageEmojiPinEvent(json);
        } else {
            return new MessageEmojiUnpinEvent(json);
        }
    }

    @Override
    public String toString() {
        return "MessageEmojiEvent{" +
                "channelId='" + channelId + '\'' +
                ", msgId='" + msgId + '\'' +
                ", userId=" + userId +
                ", emoji='" + emoji + '\'' +
                ", append=" + append +
                '}';
    }
}
