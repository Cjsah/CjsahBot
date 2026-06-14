package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Optional;

@Getter
public class ImageMessageNode extends MessageNode {
    public static final Codec<ImageMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(ImageMessageNode::getFile),
        Codec.STRING.optionalFieldOf("url").forGetter(ImageMessageNode::getUrl),
        Codec.STRING.optionalFieldOf("summary").forGetter(ImageMessageNode::getSummary),
        Codec.INT.optionalFieldOf("sub_type").forGetter(ImageMessageNode::getSubType),
        Codec.INT.optionalFieldOf("file_size").forGetter(ImageMessageNode::getFileSize)
    ).apply(instance, ImageMessageNode::new));

    private final String file;
    private final Optional<String> url;
    private final Optional<String> summary;
    private final Optional<Integer> subType;
    private final Optional<Integer> fileSize;

    public ImageMessageNode(String file, Optional<String> url, Optional<String> summary, Optional<Integer> subType) {
        super(MessageNodeType.IMAGE);
        this.file = file;
        this.url = url;
        this.summary = summary;
        this.subType = subType;
        this.fileSize = Optional.empty();
    }

    private ImageMessageNode(String file, Optional<String> url, Optional<String> summary, Optional<Integer> subType, Optional<Integer> fileSize) {
        super(MessageNodeType.IMAGE);
        this.file = file;
        this.url = url;
        this.summary = summary;
        this.subType = subType;
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return this.pair("image", this.file);
    }
}
