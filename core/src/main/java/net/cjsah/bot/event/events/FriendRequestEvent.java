package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.util.EnumUtil;

public class FriendRequestEvent extends BotEvent {
    private final long userId;
    private final String comment;
    private final String flag;

    public FriendRequestEvent(JSONObject raw) {
        super(raw);
        this.userId = raw.getLongValue("user_id");
        this.comment = raw.getString("comment");
        this.flag = raw.getString("flag");
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
        return "FriendRequestEvent{" +
                "userId=" + userId +
                ", comment='" + comment + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
