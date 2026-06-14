package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

public class AnonymousMessageNode extends MessageNode {
    public AnonymousMessageNode() {
        super(MessageNodeType.ANONYMOUS);
    }

    @Override
    public void serializeData(JSONObject json) {
    }
}
