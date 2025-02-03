package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class GroupMuteEvent extends BotEvent {
    private final long groupId;
    private final long operatorId;
    private final long userId;
    private final long duration;

    public GroupMuteEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.operatorId = raw.getLongValue("operator_id");
        this.userId = raw.getLongValue("user_id");
        this.duration = raw.getLongValue("duration");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getOperatorId() {
        return this.operatorId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "GroupMuteEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", operatorId=" + operatorId +
                ", userId=" + userId +
                ", duration=" + duration +
                '}';
    }
}
