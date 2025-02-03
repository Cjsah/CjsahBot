package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.GroupMsgMode;
import net.cjsah.bot.data.enums.MessageType;
import org.jetbrains.annotations.Nullable;

public class GroupMessageEvent extends MessageEvent {
    private final GroupMsgMode mode;
    private final long groupId;
    @Nullable
    private final JSONObject anonymous;

    public GroupMessageEvent(JSONObject raw) {
        super(raw, MessageType.GROUP);
        this.mode = GroupMsgMode.valueOf(raw.getString("sub_type").toUpperCase());
        this.groupId = raw.getLongValue("group_id");
        this.anonymous = raw.getJSONObject("anonymous");
    }

    public GroupMsgMode getMode() {
        return this.mode;
    }

    public long getGroupId() {
        return this.groupId;
    }

    @Nullable
    public JSONObject getAnonymous() {
        return this.anonymous;
    }
}
