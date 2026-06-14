package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

@Getter
public class RecordMessageNode extends MessageNode {
    public static final Codec<RecordMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(RecordMessageNode::getFile),
        Codec.INT.optionalFieldOf("file_size", 0).forGetter(RecordMessageNode::getFileSize),
        Codec.STRING.optionalFieldOf("path", "").forGetter(RecordMessageNode::getPath)
    ).apply(instance, RecordMessageNode::new));

    private final String file;
    private final int fileSize;
    private final String path;

    public RecordMessageNode(String file) {
        super(MessageNodeType.RECORD);
        this.file = file;
        this.fileSize = 0;
        this.path = "";
    }

    private RecordMessageNode(String file, int fileSize, String path) {
        super(MessageNodeType.RECORD);
        this.file = file;
        this.fileSize = fileSize;
        this.path = path;
    }

    @Override
    public String toString() {
        return this.pair("record", this.file);
    }

}
