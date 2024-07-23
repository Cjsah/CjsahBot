package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


@Getter
@ToString
public abstract class MessageNode {
    private static final Map<String, Function<JSONObject, MessageNode>> NODES = new HashMap<>();
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
        return array.toList(JSONObject.class).stream().parallel().map(json -> {
            String typeStr = json.getString("type");
            MessageType type = Arrays.stream(MessageType.values())
                    .filter(it -> it.getValue().equals(typeStr)).
                    findFirst().orElse(null);
            if (type == null) return null;
            return type.getFactory().apply(json);
        }).filter(Objects::nonNull).collect(MessageChainImpl.list());
    }
}
