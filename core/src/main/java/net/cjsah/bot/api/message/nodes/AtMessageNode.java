package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.api.message.MessageNodeType;

public class AtMessageNode extends MessageNode {
    public static final Codec<AtMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("qq").forGetter(AtMessageNode::getQQ)
    ).apply(instance, AtMessageNode::new));


    private final String qq;

    /**
     * @param qq 要at的qq号, 'all'为at所有人
     */
    public AtMessageNode(String qq) {
        super(MessageNodeType.AT);
        this.qq = qq;
    }

    public String getQQ() {
        return this.qq;
    }

    @Override
    public String toString() {
        return this.pair("at", this.qq);
    }

}
