package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

public class ReplyMessageNode extends MessageNode {
    private final int messageId;

    public ReplyMessageNode(int messageId) {
        super(MessageNodeType.REPLY);
        this.messageId = messageId;
    }

    public ReplyMessageNode(JSONObject json) {
        super(MessageNodeType.REPLY);
        this.messageId = this.parseToInt(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("id", String.valueOf(this.messageId));
    }

    @Override
    public String toString() {
        return this.toString("reply", this.messageId);
    }

}
