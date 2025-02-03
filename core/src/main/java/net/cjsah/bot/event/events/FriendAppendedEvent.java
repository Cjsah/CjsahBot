package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class FriendAppendedEvent extends BotEvent {
    private final long userId;

    public FriendAppendedEvent(JSONObject raw) {
        super(raw);
        this.userId = raw.getLongValue("user_id");
    }

    public long getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "FriendAppendedEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", userId=" + userId +
                '}';
    }
}
