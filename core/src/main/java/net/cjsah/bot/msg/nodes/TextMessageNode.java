package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

public class TextMessageNode extends MessageNode {
    public static final MessageNode EMPTY = new TextMessageNode("");
    private final String text;

    public TextMessageNode(String text) {
        super(MessageNodeType.TEXT);
        this.text = text;
    }

    public TextMessageNode(JSONObject json) {
        this(json.getString("text"));
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("text", this.text);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
