package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class XMLMessageNode extends MessageNode {
    private final String xml;

    public XMLMessageNode(String xml) {
        super(MessageType.XML);
        this.xml = xml;
    }

    public XMLMessageNode(JSONObject json) {
        this(json.getString("data"));
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("data", this.xml);
    }
}
