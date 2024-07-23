package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Getter
@ToString
public abstract class MessageNode {
    private static final Map<String, Function<JSONObject, MessageNode>> nodes = new HashMap<>();
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

    protected int parseToInt(JSONObject json, String key) {
        String val = json.getString(key);
        return Integer.parseInt(val);
    }

    protected long parseToLong(JSONObject json, String key) {
        String val = json.getString(key);
        return Long.parseLong(val);
    }
    protected float parseToFloat(JSONObject json, String key) {
        String val = json.getString(key);
        return Float.parseFloat(val);
    }

    public static MessageChain parseMessage(JSONArray array) {

    }
}
