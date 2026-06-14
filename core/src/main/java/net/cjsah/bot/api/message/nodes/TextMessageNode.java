package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class TextMessageNode extends MessageNode {
    public static final Codec<TextMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("text").forGetter(TextMessageNode::getText)
    ).apply(instance, TextMessageNode::new));
    public static final MessageNode EMPTY = new TextMessageNode("");
    private final String text;

    public TextMessageNode(String text) {
        super(MessageNodeType.TEXT);
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
