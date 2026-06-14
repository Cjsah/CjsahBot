package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Optional;

@Getter
public class FaceMessageNode extends MessageNode {
    public static final Codec<FaceMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(FaceMessageNode::getId),
        Codec.STRING.optionalFieldOf("resultId").forGetter(FaceMessageNode::getResultId),
        Codec.INT.optionalFieldOf("chainCount").forGetter(FaceMessageNode::getChainCount)
    ).apply(instance, FaceMessageNode::new));

    private final String id;
    private final Optional<String> resultId;
    private final Optional<Integer> chainCount;

    public FaceMessageNode(String id) {
        super(MessageNodeType.FACE);
        this.id = id;
        this.resultId = Optional.empty();
        this.chainCount = Optional.empty();
    }

    private FaceMessageNode(String id, Optional<String> resultId, Optional<Integer> chainCount) {
        super(MessageNodeType.FACE);
        this.id = id;
        this.resultId = resultId;
        this.chainCount = chainCount;
    }

    @Override
    public String toString() {
        return this.pair("face", this.id);
    }
}
