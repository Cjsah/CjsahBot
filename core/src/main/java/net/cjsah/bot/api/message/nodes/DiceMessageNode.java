package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

public class DiceMessageNode extends MessageNode {
    public DiceMessageNode() {
        super(MessageNodeType.DICE);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
