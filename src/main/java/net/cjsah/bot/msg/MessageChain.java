package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.nodes.TextMessageNode;

import java.util.List;

public interface MessageChain extends List<MessageNode> {

    static MessageChain raw(String text) {
        TextMessageNode node = new TextMessageNode(text);
        return new MessageChainImpl(node);
    }

    default List<JSONObject> toJson() {
        return this.stream().map(it -> {
            JSONObject json = new JSONObject();
            it.serialize(json);
            return json;
        }).toList();
    }
}
