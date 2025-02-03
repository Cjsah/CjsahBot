package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.HonorType;
import net.cjsah.bot.util.EnumUtil;

public class GroupHonorEvent extends BotEvent {
    private final long groupId;
    private final long userId;
    private final HonorType honorType;

    public GroupHonorEvent(JSONObject raw) {
        super(raw);
        this.groupId = raw.getLongValue("group_id");
        this.userId = raw.getLongValue("user_id");
        this.honorType = EnumUtil.ofName(HonorType.class, raw.getString("honor_type"));
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public HonorType getHonorType() {
        return this.honorType;
    }

    @Override
    public String toString() {
        return "GroupHonorEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", honorType=" + honorType +
                '}';
    }
}
