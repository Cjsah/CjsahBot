package net.cjsah.bot.api.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.api.message.nodes.*;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum MessageNodeType implements IStrSerializable {
    TEXT("text", TextMessageNode.CODEC),                // 纯文本
    AT("at", AtMessageNode.CODEC),                      // at
    REPLY("reply", ReplyMessageNode.CODEC),             // 回复
    FACE("face", FaceMessageNode.CODEC),                // QQ 表情
    MFACE("mface", MFaceMessageNode.CODEC),                // QQ 表情
    DICE("dice", DiceMessageNode.CODEC),                // 掷骰子
    RPS("rps", RpsMessageNode.CODEC),                   // 猜拳
    POKE("poke", PokeMessageNode.CODEC),                // 戳一戳
    IMAGE("image", ImageMessageNode.CODEC),             // 图片
    RECORD("record", RecordMessageNode.CODEC),          // 语音
    VIDEO("video", VideoMessageNode.CODEC),             // 短视频
    FILE("file", FileMessageNode.CODEC),             // 短视频
    JSON("json", JsonMessageNode.CODEC),                // json消息
    FORWARD("forward", ForwardMessageNode.CODEC),       // 合并转发
    ;

    public static final Codec<MessageNodeType> CODEC = IStrSerializable.fromEnum(MessageNodeType.class);
    private final String type;
    private final Codec<? extends MessageNode> codec;

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public Codec<? extends MessageNode> codec() {
        return this.codec;
    }

    public record Builder(MessageNodeType type) {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MessageNodeType.CODEC.fieldOf("notice_type").forGetter(Builder::type)
        ).apply(instance, MessageNodeType.Builder::new));

        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
