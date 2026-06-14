package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mojang.serialization.Codec;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.api.message.MessageChainImpl;
import net.cjsah.bot.api.message.MessageNodeType;
import net.cjsah.bot.util.StringUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class MessageNode {
    public static final Codec<MessageNode> CODEC = null;
    private final MessageNodeType type;

    protected MessageNode(MessageNodeType type) {
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

    protected String parsetoString(JSONObject json, String key) {
        return this.parsetoString(json, key, false);
    }

    protected String parsetoString(JSONObject json, String key, boolean isUrl) {
        String val = json.getString(key);
        return isUrl ? StringUtil.netReplace(val) : val;
    }

    public String toString() {
        return "[" + this.type.getValue() + "]";
    }

    protected String toString(String key, Object value) {
        return "[" + key + "=" + value + "]";
    }

    protected String toString(String name, Map<String, Object> map) {
        String content = map.entrySet().stream()
                .map(it -> it.getKey() + "=" + it.getValue())
                .collect(Collectors.joining(",", "(", ")"));
        return "[" + name + content + "]";
    }

    public static MessageChain parseMessage(JSONArray array) {
        return array.toList(JSONObject.class).stream().parallel().map(json -> {
            String typeStr = json.getString("type");
            MessageNodeType type = Arrays.stream(MessageNodeType.values())
                    .filter(it -> it.getValue().equals(typeStr)).
                    findFirst().orElse(null);
            if (type == null) return null;
            return type.getFactory().apply(json.getJSONObject("data"));
        }).filter(Objects::nonNull).collect(MessageChainImpl.list());
    }
}
