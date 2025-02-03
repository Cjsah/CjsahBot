package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.CountStatus;

public class GroupAdminChangeEvent extends BotEvent {
    protected final long groupId;
    protected final long userId;
    protected final CountStatus type;

    public GroupAdminChangeEvent(JSONObject raw, CountStatus type) {
        super(raw);
        this.type = type;
        this.groupId = raw.getLongValue("group_id");
        this.userId = raw.getLongValue("user_id");
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public CountStatus getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "GroupAdminChangeEvent{" +
                "time=" + time +
                ", selfId=" + selfId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", type=" + type +
                '}';
    }
}
