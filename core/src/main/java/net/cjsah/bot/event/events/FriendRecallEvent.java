package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class FriendRecallEvent extends MessageRecallEvent {
    public FriendRecallEvent(JSONObject raw) {
        super(raw);
    }
}
