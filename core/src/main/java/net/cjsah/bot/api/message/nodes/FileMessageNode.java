package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class FileMessageNode extends MessageNode {
    public static final Codec<FileMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(FileMessageNode::getFile),
        Codec.INT.optionalFieldOf("file_size", 0).forGetter(FileMessageNode::getFileSize),
        Codec.STRING.optionalFieldOf("file_id", "").forGetter(FileMessageNode::getFileId)
    ).apply(instance, FileMessageNode::new));

    private final String file;
    private final int fileSize;
    private final String fileId;

    public FileMessageNode(String file) {
        super(MessageNodeType.VIDEO);
        this.file = file;
        this.fileSize = 0;
        this.fileId = "";
    }

    private FileMessageNode(String file, int fileSize, String fileId) {
        super(MessageNodeType.VIDEO);
        this.file = file;
        this.fileSize = fileSize;
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return this.pair("video", this.file);
    }

}
