package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class ReplyMessageNode extends MessageNode {
    public static final Codec<ReplyMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(ReplyMessageNode::getMessageId)
    ).apply(instance, ReplyMessageNode::new));

    private final String messageId;

    public ReplyMessageNode(String messageId) {
        super(MessageNodeType.REPLY);
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return this.pair("reply", this.messageId);
    }

}
