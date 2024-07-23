package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class ShakeMessageNode extends MessageNode {
    public ShakeMessageNode() {
        super(MessageType.SHAKE);
    }

    @Override
    public void serializeData(JSONObject json) {}
}
