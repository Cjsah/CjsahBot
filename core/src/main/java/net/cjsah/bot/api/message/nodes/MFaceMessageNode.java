package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Optional;

@Getter
public class MFaceMessageNode extends MessageNode {
    public static final Codec<MFaceMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("emoji_id").forGetter(MFaceMessageNode::getEmojiId),
        Codec.STRING.fieldOf("emoji_package_id").forGetter(MFaceMessageNode::getEmojiPackageId),
        Codec.STRING.optionalFieldOf("key").forGetter(MFaceMessageNode::getKey),
        Codec.STRING.optionalFieldOf("summary").forGetter(MFaceMessageNode::getSummary)
    ).apply(instance, MFaceMessageNode::new));

    private final String emojiId;
    private final String emojiPackageId;
    private final Optional<String> key;
    private final Optional<String> summary;

    public MFaceMessageNode(String emojiId, String emojiPackageId, Optional<String> key, Optional<String> summary) {
        super(MessageNodeType.FACE);
        this.emojiId = emojiId;
        this.emojiPackageId = emojiPackageId;
        this.key = key;
        this.summary = summary;
    }

    @Override
    public String toString() {
        return this.pair("mface", this.emojiId);
    }
}
