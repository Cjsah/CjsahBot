package net.cjsah.bot.event.events;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.UserData;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.util.StringUtil;

import java.util.function.Function;

public class MessageEvent<T extends UserData> extends BotEvent {
    protected final MessageType msgType;
    protected final int msgId;
    protected final long userId;
    protected final String rawMsg;
    protected final MessageChain message;
    protected final T sender;
    protected final int font;

    protected MessageEvent(JSONObject raw, MessageType type, Function<JSONObject, T> factory) {
        super(raw);
        this.msgType = type;
        this.msgId = raw.getIntValue("message_id");
        this.userId = raw.getLongValue("user_id");
        this.rawMsg = StringUtil.netReplace(raw.getString("raw_message"));
        this.font = raw.getIntValue("font");
        this.message = MessageChain.parse(raw.getJSONArray("message"));
        this.sender = factory.apply(raw.getJSONObject("sender"));
    }

    public MessageType getMsgType() {
        return this.msgType;
    }

    public int getMsgId() {
        return this.msgId;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getRawMsg() {
        return this.rawMsg;
    }

    public MessageChain getMessage() {
        return this.message;
    }

    public T getSender() {
        return this.sender;
    }

    public int getFont() {
        return this.font;
    }
}
