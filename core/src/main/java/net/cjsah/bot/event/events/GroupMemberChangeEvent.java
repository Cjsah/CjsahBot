package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;

public class GroupMemberChangeEvent extends BotEvent {
    protected final CountStatus type;
    protected final long groupId;
    protected final long userId;
    protected final long operatorId;

    public GroupMemberChangeEvent(JSONObject raw, CountStatus type) {
        super(raw);
        this.type = type;
        this.groupId = raw.getLongValue("group_id");
        this.operatorId = raw.getLongValue("operator_id");
        this.userId = raw.getLongValue("user_id");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public CountStatus getType() {
        return this.type;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getOperatorId() {
        return this.operatorId;
    }

    @Override
    public String toString() {
        return "GroupMemberChangeEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", type=" + type +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", operatorId=" + operatorId +
                '}';
    }
}
