package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class VideoMessageNode extends MessageNode {
    public static final Codec<VideoMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(VideoMessageNode::getFile),
        Codec.INT.optionalFieldOf("file_size", 0).forGetter(VideoMessageNode::getFileSize),
        Codec.STRING.optionalFieldOf("url", "").forGetter(VideoMessageNode::getUrl)
    ).apply(instance, VideoMessageNode::new));

    private final String file;
    private final int fileSize;
    private final String url;

    public VideoMessageNode(String file) {
        super(MessageNodeType.VIDEO);
        this.file = file;
        this.fileSize = 0;
        this.url = "";
    }

    private VideoMessageNode(String file, int fileSize, String url) {
        super(MessageNodeType.VIDEO);
        this.file = file;
        this.fileSize = fileSize;
        this.url = url;
    }

    @Override
    public String toString() {
        return this.pair("video", this.file);
    }

}
