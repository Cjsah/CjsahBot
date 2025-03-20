package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class FriendMessageEvent extends MessageEvent {
    public FriendMessageEvent(JSONObject raw) {
        super(raw);
    }
}
