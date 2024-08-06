package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.nodes.TextMessageNode;

import java.util.Collection;
import java.util.List;

public interface MessageChain extends Collection<MessageNode> {
    MessageChain EMPTY = MessageChainImpl.EMPTY;

    static MessageChain raw(String text) {
        TextMessageNode node = new TextMessageNode(text);
        return new MessageChainImpl(node);
    }

    static MessageChain parse(JSONArray array) {
        return MessageNode.parseMessage(array);
    }

    static MessageChain of(MessageNode ...nodes) {
        return new MessageChainImpl(nodes);
    }

    default List<JSONObject> toJson() {
        return this.stream().map(it -> {
            JSONObject json = new JSONObject();
            it.serialize(json);
            return json;
        }).toList();
    }
}
