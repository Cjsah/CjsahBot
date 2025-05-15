package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.FriendCommandSource;
import net.cjsah.bot.data.UserData;
import net.cjsah.bot.data.enums.FriendMsgMode;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.util.EnumUtil;

public class FriendMessageEvent extends MessageEvent<UserData> {
    private final FriendMsgMode mode;

    public FriendMessageEvent(JSONObject raw) {
        super(raw, MessageType.FRIEND, UserData::new);
        this.mode = EnumUtil.ofName(FriendMsgMode.class, raw.getString("sub_type"), FriendMsgMode.OTHER);
    }

    public FriendMsgMode getMode() {
        return this.mode;
    }

    @Override
    public CommandSource<?> genCommandSource() {
        return new FriendCommandSource(this);
    }
}
