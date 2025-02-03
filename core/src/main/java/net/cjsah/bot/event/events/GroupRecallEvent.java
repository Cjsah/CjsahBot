package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class GroupRecallEvent extends MessageRecallEvent {
    private final long groupId;
    private final long operatorId;

    public GroupRecallEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.operatorId = raw.getLongValue("operator_id");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getOperatorId() {
        return this.operatorId;
    }

    @Override
    public String toString() {
        return "GroupRecallEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", operatorId=" + operatorId +
                ", messageId=" + messageId +
                '}';
    }
}
