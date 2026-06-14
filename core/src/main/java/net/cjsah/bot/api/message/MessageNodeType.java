package net.cjsah.bot.api.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.cjsah.bot.api.message.nodes.*;
import net.cjsah.bot.data.IStrSerializable;

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
    private final Codec<MessageNode> codec;
    private final MapCodec<MessageNode> mapCodec;

    @SuppressWarnings("unchecked")
    MessageNodeType(String type, Codec<? extends MessageNode> codec) {
        this.type = type;
        this.codec = (Codec<MessageNode>) codec;
        this.mapCodec = this.codec.fieldOf("data");
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public Codec<MessageNode> codec() {
        return this.codec;
    }

    public MapCodec<MessageNode> mapCodec() {
        return this.mapCodec;
    }
}
