package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class GroupAtMessageEvent extends MessageEvent {
    private final String groupOpenId;

    public GroupAtMessageEvent(JSONObject raw) {
        super(raw);
        this.groupOpenId = raw.getString("group_openid");
    }

    public String getGroupId() {
        return this.groupOpenId;
    }
}
