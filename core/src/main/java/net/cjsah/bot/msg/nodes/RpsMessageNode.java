package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

public class RpsMessageNode extends MessageNode {
    public RpsMessageNode() {
        super(MessageNodeType.RPS);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
