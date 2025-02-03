package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class GroupLuckyKingEvent extends BotEvent {
    private final long groupId;
    private final long userId;
    private final long kingId;

    public GroupLuckyKingEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.userId = raw.getLongValue("user_id");
        this.kingId = raw.getLongValue("target_id");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getKingId() {
        return this.kingId;
    }

    @Override
    public String toString() {
        return "GroupLuckyKingEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", kingId=" + kingId +
                '}';
    }
}
