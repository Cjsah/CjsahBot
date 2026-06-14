package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

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
