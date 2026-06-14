package net.cjsah.bot.api.message.nodes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;
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


    public String toString() {
        return "[" + this.type.getSerializedName() + "]";
    }

    protected String pair(String key, Object value) {
        return "[" + key + "=" + value + "]";
    }

    protected String pair(String name, Map<String, Object> map) {
        String content = map.entrySet().stream()
            .map(it -> it.getKey() + "=" + it.getValue())
            .collect(Collectors.joining(",", "(", ")"));
        return "[" + name + content + "]";
    }
}
