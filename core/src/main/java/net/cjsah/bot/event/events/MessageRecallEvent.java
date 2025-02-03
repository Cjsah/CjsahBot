package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class MessageRecallEvent extends BotEvent {
    protected final long userId;
    protected final long messageId;

    public MessageRecallEvent(JSONObject raw) {
        super(raw);
        this.userId = raw.getLongValue("user_id");
        this.messageId = raw.getLongValue("message_id");
    }

    public long getUserId() {
        return this.userId;
    }

    public long getMessageId() {
        return this.messageId;
    }

    @Override
    public String toString() {
        return "MessageRecallEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", userId=" + userId +
                ", messageId=" + messageId +
                '}';
    }
}
