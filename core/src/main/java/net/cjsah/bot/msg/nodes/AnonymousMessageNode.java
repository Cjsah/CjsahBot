package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

public class AnonymousMessageNode extends MessageNode {
    public AnonymousMessageNode() {
        super(MessageNodeType.ANONYMOUS);
    }

    @Override
    public void serializeData(JSONObject json) {
    }
}
