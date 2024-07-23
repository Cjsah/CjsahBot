package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.nodes.TextMessageNode;

import java.util.List;

public interface MessageChain extends List<MessageNode> {
    MessageChain EMPTY = MessageChainImpl.EMPTY;

    static MessageChain raw(String text) {
        TextMessageNode node = new TextMessageNode(text);
        return new MessageChainImpl(node);
    }

    static MessageChain parse(JSONArray array) {
        return MessageNode.parseMessage(array);
    }

    default List<JSONObject> toJson() {
        return this.stream().map(it -> {
            JSONObject json = new JSONObject();
            it.serialize(json);
            return json;
        }).toList();
    }
}
