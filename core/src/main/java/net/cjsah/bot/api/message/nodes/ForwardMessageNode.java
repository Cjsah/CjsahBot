package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class ForwardMessageNode extends MessageNode {
    public static final Codec<ForwardMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(ForwardMessageNode::getMessageId)
    ).apply(instance, ForwardMessageNode::new));

    private final String messageId;

    public ForwardMessageNode(String messageId) {
        super(MessageNodeType.FORWARD);
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return this.pair("forward", this.messageId);
    }
}
