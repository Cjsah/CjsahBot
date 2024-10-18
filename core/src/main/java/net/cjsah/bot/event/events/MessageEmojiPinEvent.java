package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class MessageEmojiPinEvent extends MessageEmojiEvent {

    public MessageEmojiPinEvent(JSONObject json) {
        super(json, true);
    }

    @Override
    public String toString() {
        return "MessageEmojiPinEvent{} " + super.toString();
    }
}
