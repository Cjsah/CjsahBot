package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class DiceMessageNode extends MessageNode {
    public static final Codec<DiceMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("result", "").forGetter(DiceMessageNode::getResult)
    ).apply(instance, DiceMessageNode::new));

    private final String result;

    public DiceMessageNode() {
        super(MessageNodeType.DICE);
        this.result = "";
    }

    private DiceMessageNode(String result) {
        super(MessageNodeType.DICE);
        this.result = result;
    }
}
