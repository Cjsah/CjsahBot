package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class ReplyMessageNode extends MessageNode {
    private final int messageId;

    public ReplyMessageNode(int messageId) {
        super(MessageType.REPLY);
        this.messageId = messageId;
    }

    public ReplyMessageNode(JSONObject json) {
        super(MessageType.REPLY);
        this.messageId = this.parseToInt(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("id", String.valueOf(this.messageId));
    }
}
