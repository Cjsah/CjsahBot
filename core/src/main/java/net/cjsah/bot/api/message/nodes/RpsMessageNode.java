package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

public class RpsMessageNode extends MessageNode {
    public RpsMessageNode() {
        super(MessageNodeType.RPS);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
