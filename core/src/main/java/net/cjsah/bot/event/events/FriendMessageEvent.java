package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.FriendMsgMode;
import net.cjsah.bot.data.enums.MessageType;

public class FriendMessageEvent extends MessageEvent {
    private final FriendMsgMode mode;

    public FriendMessageEvent(JSONObject raw) {
        super(raw, MessageType.FRIEND);
        this.mode = FriendMsgMode.valueOf(raw.getString("sub_type").toUpperCase());
    }

    public FriendMsgMode getMode() {
        return this.mode;
    }
}
