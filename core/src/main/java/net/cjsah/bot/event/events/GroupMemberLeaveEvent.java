package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;
import net.cjsah.bot.data.enums.GroupMemberLeaveType;
import net.cjsah.bot.util.EnumUtil;

public class GroupMemberLeaveEvent extends GroupMemberChangeEvent {
    private final GroupMemberLeaveType leaveType;

    public GroupMemberLeaveEvent(JSONObject raw) {
        super(raw, CountStatus.DECREASE);
        this.leaveType = EnumUtil.ofName(GroupMemberLeaveType.class, raw.getString("sub_type"));
    }

    public GroupMemberLeaveType getLeaveType() {
        return this.leaveType;
    }

    @Override
    public String toString() {
        return "GroupMemberLeaveEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", type=" + type +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", operatorId=" + operatorId +
                ", leaveType=" + leaveType +
                '}';
    }
}
