package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.GroupCommandSource;

public class GroupAtMessageEvent extends MessageEvent {
    private final String groupOpenId;

    public GroupAtMessageEvent(String eventId, JSONObject raw) {
        super(eventId, raw, true);
        this.groupOpenId = raw.getString("group_openid");
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new GroupCommandSource(this);
    }

    public String getGroupId() {
        return this.groupOpenId;
    }

    @Override
    public String toString() {
        return "GroupAtMessageEvent{super=" + super.toString() + ", groupOpenId='" + groupOpenId + "'}";
    }

}
