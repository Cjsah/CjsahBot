package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
public class RpsMessageNode extends MessageNode {
    public RpsMessageNode() {
        super(MessageType.RPS);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
