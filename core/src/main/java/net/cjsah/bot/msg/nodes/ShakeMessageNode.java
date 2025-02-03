package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

public class ShakeMessageNode extends MessageNode {
    public ShakeMessageNode() {
        super(MessageNodeType.SHAKE);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
