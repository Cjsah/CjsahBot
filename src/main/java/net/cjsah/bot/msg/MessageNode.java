package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;


@Getter
@ToString
public abstract class MessageNode {
    private final MessageType type;

    protected MessageNode(MessageType type) {
        this.type = type;
    }

    public void serialize(JSONObject json) {
        json.put("type", this.type.getValue());
        JSONObject data = json.putObject("data");
        this.serializeData(data);
    }

    protected abstract void serializeData(JSONObject json);
}
