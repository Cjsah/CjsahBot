package net.cjsah.bot.api.message.nodes;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;
import net.cjsah.bot.util.CodecUtil;

@Getter
public class JsonMessageNode extends MessageNode {
    public static final Codec<JsonMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CodecUtil.JSON.fieldOf("data").forGetter(JsonMessageNode::getJson)
    ).apply(instance, JsonMessageNode::new));


    private final JsonElement json;

    public JsonMessageNode(JsonElement json) {
        super(MessageNodeType.JSON);
        this.json = json;
    }

    @Override
    public String toString() {
        return this.pair("json", this.json);
    }
}
