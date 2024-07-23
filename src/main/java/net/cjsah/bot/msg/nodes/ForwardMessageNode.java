package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class ForwardMessageNode extends MessageNode {
    private final int messageId;

    /**
     * 需要通过 {@linkplain net.cjsah.bot.api.Api#getForwardMsg(String)[Api.getForwardMsg]}获取具体内容
     */
    public ForwardMessageNode(JSONObject json) {
        super(MessageType.FORWARD);
        this.messageId = this.parseToInt(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("id", String.valueOf(this.messageId));
    }
}
