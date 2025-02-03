package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageChain;

public class MessageEvent extends BotEvent {
    protected final MessageType msgType;
    protected final int msgId;
    protected final long userId;
    protected final String rawMsg;
    protected final MessageChain message;
    protected final JSONObject sender;
    protected final int font;

    protected MessageEvent(JSONObject raw, MessageType type) {
        super(raw);
        this.msgType = type;
        this.msgId = raw.getIntValue("message_id");
        this.userId = raw.getLongValue("user_id");
        this.rawMsg = raw.getString("raw_message");
        this.font = raw.getIntValue("font");
        this.message = MessageChain.parse(raw.getJSONArray("message"));
        this.sender = raw.getJSONObject("sender");
    }

}
