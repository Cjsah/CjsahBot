package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class ImageMessageNode extends MessageNode {
    public static final Codec<ImageMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(ImageMessageNode::getFile),
        Codec.STRING.optionalFieldOf("url", "").forGetter(ImageMessageNode::getUrl),
        Codec.STRING.optionalFieldOf("summary", "").forGetter(ImageMessageNode::getSummary),
        Codec.INT.optionalFieldOf("sub_type", 0).forGetter(ImageMessageNode::getSubType),
        Codec.STRING.optionalFieldOf("file_size", "0").forGetter(ImageMessageNode::getFileSize)
    ).apply(instance, ImageMessageNode::new));

    private final String file;
    private final String url;
    private final String summary;
    private final String fileSize;
    private final Integer subType;

    public ImageMessageNode(String file) {
        this(file, "", "", 0, "0");
    }

    public ImageMessageNode(String file, String url, String summary, Integer subType) {
        this(file, url, summary, subType, "0");
    }

    private ImageMessageNode(String file, String url, String summary, Integer subType, String fileSize) {
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
