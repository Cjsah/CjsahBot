package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

public class ShakeMessageNode extends MessageNode {
    public ShakeMessageNode() {
        super(MessageNodeType.SHAKE);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
