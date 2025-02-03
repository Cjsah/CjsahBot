package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;

public class GroupMuteEvent extends BotEvent {
    private final long groupId;
    private final long operatorId;
    private final CountStatus type;
    private final long userId;
    private final long duration;

    public GroupMuteEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.operatorId = raw.getLongValue("operator_id");
        this.userId = raw.getLongValue("user_id");
        this.duration = raw.getLongValue("duration");
        String type = raw.getString("sub_type");
        switch (type) {
            case "ban" -> this.type = CountStatus.INCREASE;
            case "lift_ban" -> this.type = CountStatus.DECREASE;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public CountStatus getType() {
        return this.type;
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
                ", type=" + type +
                ", userId=" + userId +
                ", duration=" + duration +
                '}';
    }
}
