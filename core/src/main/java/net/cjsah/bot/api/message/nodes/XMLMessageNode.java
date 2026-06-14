package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

public class XMLMessageNode extends MessageNode {
    private final String xml;

    public XMLMessageNode(String xml) {
        super(MessageNodeType.XML);
        this.xml = xml;
    }

    public XMLMessageNode(JSONObject json) {
        this(json.getString("data"));
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("data", this.xml);
    }

    @Override
    public String toString() {
        return this.toString("xml", this.xml);
    }
}
