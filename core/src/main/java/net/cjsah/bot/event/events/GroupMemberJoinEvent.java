package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.data.enums.GroupMemberJoinType;
import net.cjsah.bot.util.EnumUtil;

public class GroupMemberJoinEvent extends GroupMemberChangeEvent {
    private final GroupMemberJoinType joinType;

    public GroupMemberJoinEvent(JSONObject raw) {
        super(raw, CountStatus.INCREASE);
        this.joinType = EnumUtil.ofName(GroupMemberJoinType.class, raw.getString("sub_type"));
    }

    public GroupMemberJoinType getJoinType() {
        return this.joinType;
    }

    @Override
    public String toString() {
        return "GroupMemberJoinEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", type=" + type +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", operatorId=" + operatorId +
                ", joinType=" + joinType +
                '}';
    }
}
