package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

public class DiceMessageNode extends MessageNode {
    public DiceMessageNode() {
        super(MessageNodeType.DICE);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
