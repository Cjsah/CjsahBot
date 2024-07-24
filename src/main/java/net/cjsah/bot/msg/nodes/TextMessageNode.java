package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class TextMessageNode extends MessageNode {
    public static final MessageNode EMPTY = new TextMessageNode("");
    private final String text;

    public TextMessageNode(String text) {
        super(MessageType.TEXT);
        this.text = text;
    }

    public TextMessageNode(JSONObject json) {
        this(json.getString("text"));
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("text", this.text);
    }
}
