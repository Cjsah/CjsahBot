package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;

public class MessageEmojiUnpinEvent extends MessageEmojiEvent {

    public MessageEmojiUnpinEvent(JSONObject json) {
        super(json, false);
    }

    @Override
    public String toString() {
        return "MessageEmojiUnpinEvent{} " + super.toString();
    }
}
