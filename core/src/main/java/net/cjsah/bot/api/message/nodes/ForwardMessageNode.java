package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

public class ForwardMessageNode extends MessageNode {
    private final String messageId;

    /**
     * 需要通过 {@linkplain net.cjsah.bot.api.Api#getForwardMsg(String)[Api.getForwardMsg]}获取具体内容
     */
    public ForwardMessageNode(JSONObject json) {
        super(MessageNodeType.FORWARD);
        this.messageId = this.parsetoString(json, "id", false);
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("id", this.messageId);
    }

    @Override
    public String toString() {
        return this.toString("forward", this.messageId);
    }
}
