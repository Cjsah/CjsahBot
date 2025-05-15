package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.GroupCommandSource;
import net.cjsah.bot.data.AnonymousUserData;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.enums.GroupMsgMode;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.util.EnumUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupMessageEvent extends MessageEvent<GroupUserData> {
    private static final Logger log = LoggerFactory.getLogger(GroupMessageEvent.class);
    private final GroupMsgMode mode;
    private final long groupId;
    private final String groupName;
    @Nullable
    private final AnonymousUserData anonymous;

    public GroupMessageEvent(JSONObject raw) {
        super(raw, MessageType.GROUP, GroupUserData::new);
        this.mode = EnumUtil.ofName(GroupMsgMode.class, raw.getString("sub_type"));
        this.groupId = raw.getLongValue("group_id");
        this.groupName = raw.getJSONObject("raw").getString("peerName");
        JSONObject anonymous = raw.getJSONObject("anonymous");
        this.anonymous = anonymous == null ? null : new AnonymousUserData(anonymous);
    }

    public GroupMsgMode getMode() {
        return this.mode;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    @Nullable
    public AnonymousUserData getAnonymous() {
        return this.anonymous;
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new GroupCommandSource(this);
    }
}
