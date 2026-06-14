package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class RpsMessageNode extends MessageNode {
    public static final Codec<RpsMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("result", "").forGetter(RpsMessageNode::getResult)
    ).apply(instance, RpsMessageNode::new));

    private final String result;

    public RpsMessageNode() {
        super(MessageNodeType.RPS);
        this.result = "";
    }

    private RpsMessageNode(String result) {
        super(MessageNodeType.RPS);
        this.result = result;
    }
}
