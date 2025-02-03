package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.JoinType;
import net.cjsah.bot.util.EnumUtil;

public class GroupRequestEvent extends BotEvent {
    private final JoinType requestType;
    private final long groupId;
    private final long userId;
    private final String comment;
    private final String flag;

    public GroupRequestEvent(JSONObject raw) {
        super(raw);
        this.requestType = EnumUtil.ofName(JoinType.class, raw.getString("sub_type"));
        this.groupId = raw.getLongValue("group_id");
        this.userId = raw.getLongValue("user_id");
        this.comment = raw.getString("comment");
        this.flag = raw.getString("flag");
    }

    public JoinType getRequestType() {
        return this.requestType;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getComment() {
        return this.comment;
    }

    public String getFlag() {
        return this.flag;
    }

    @Override
    public String toString() {
        return "GroupRequestEvent{" +
                "requestType=" + requestType +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", comment='" + comment + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
