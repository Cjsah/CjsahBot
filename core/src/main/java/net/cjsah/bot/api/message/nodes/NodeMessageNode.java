package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;

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
        super(MessageNodeType.NODE);
        this.messageId = messageId;
        this.qq = 0;
        this.nickname = null;
        this.message = null;
    }

    /**
     * 合并消息并发送
     */
    public NodeMessageNode(long qq, String nickname, MessageChain message) {
        super(MessageNodeType.NODE);
        this.messageId = 0;
        this.qq = qq;
        this.nickname = nickname;
        this.message = message;
    }

    public NodeMessageNode(JSONObject json) {
        super(MessageNodeType.NODE);
        throw new IllegalCallerException("不支持的操作!");
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

    @Override
    public String toString() {
        if (this.message == null) {
            return this.toString("node", this.messageId);
        } else {
            assert this.nickname != null;
            return this.toString("node", Map.of(
                    "qq", this.qq,
                    "nickname", this.nickname,
                    "content", this.message.toString()
            ));
        }

    }

}
