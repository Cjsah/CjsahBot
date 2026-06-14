package net.cjsah.bot.api.message.nodes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.api.message.MessageChainImpl;
import net.cjsah.bot.api.message.MessageNodeType;
import net.cjsah.bot.util.StringUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class MessageNode {
    public static final Codec<MessageNode> NODE_CODEC = new Codec<>() {
        private static final MapCodec<MessageNodeType> TYPE_CODEC = MessageNodeType.CODEC.fieldOf("type");

        @Override
        @SuppressWarnings("unchecked")
        public <T> DataResult<Pair<MessageNode, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input)
                .setLifecycle(Lifecycle.stable())
                .flatMap(map ->
                    TYPE_CODEC.decode(ops, map).flatMap(type ->
                        ((Codec<MessageNode>) type.codec()).decode(ops, map.get("data"))
                    )
                );
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> DataResult<T> encode(MessageNode input, DynamicOps<T> ops, T prefix) {
            return TYPE_CODEC.encoder().encode(input.type, ops, prefix).flatMap(it ->
                ((Codec<MessageNode>)input.type.codec()).fieldOf("data").encoder().encode(input, ops, it)
            );
        }
    };

    private final MessageNodeType type;

    protected MessageNode(MessageNodeType type) {
        this.type = type;
    }

    public MessageNodeType getType() {
        return this.type;
    }

    public void serialize(JSONObject json) {
        json.put("type", this.type.getSerializedName());
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
        return "[" + this.type.getSerializedName() + "]";
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
                .filter(it -> it.getSerializedName().equals(typeStr))
                .findFirst().orElse(null);
            if (type == null) return null;
            return type.getFactory().apply(json.getJSONObject("data"));
        }).filter(Objects::nonNull).collect(MessageChainImpl.list());
    }
}
