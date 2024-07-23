package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class NodeMessageNode extends MessageNode {
    /* 转发合并消息 */
    private final int messageId;
    /* 发送合并消息 */
    private final long qq;
    private final String nickname;
    private final MessageChain message;

    /**
     * 转发已合并的消息
     */
    public NodeMessageNode(int messageId) {
        super(MessageType.NODE);
        this.messageId = messageId;
        this.qq = 0;
        this.nickname = null;
        this.message = null;
    }

    /**
     * 合并消息并发送
     */
    public NodeMessageNode(long qq, String nickname, MessageChain message) {
        super(MessageType.NODE);
        this.messageId = 0;
        this.qq = qq;
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public void serializeData(JSONObject json) {
        if (this.message == null) {
            json.put("id", String.valueOf(this.messageId));
        } else {
            json.put("user_id", String.valueOf(this.qq));
            json.put("nickname", this.nickname);
            json.put("content", this.message.toJson());
        }
    }
}
