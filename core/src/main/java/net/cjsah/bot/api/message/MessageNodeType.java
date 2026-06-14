package net.cjsah.bot.api.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.api.message.nodes.*;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum MessageNodeType implements IStrSerializable {
    TEXT("text", TextMessageNode.CODEC),                // 纯文本
    FACE("face", FaceMessageNode.CODEC),                // QQ 表情
    IMAGE("image", ImageMessageNode.CODEC),             // 图片
    RECORD("record", RecordMessageNode.CODEC),          // 语音
    VIDEO("video", VideoMessageNode.CODEC),             // 短视频
    AT("at", AtMessageNode.CODEC),                      // at
    RPS("rps", RpsMessageNode.CODEC),                   // 猜拳
    DICE("dice", DiceMessageNode.CODEC),                // 掷骰子
    SHAKE("shake", ShakeMessageNode.CODEC),             // 窗口抖动
    POKE("poke", PokeMessageNode.CODEC),                // 戳一戳
    ANONYMOUS("anonymous", AnonymousMessageNode.CODEC), // 匿名消息
    SHARE("share", ShakeMessageNode.CODEC),             // 链接分享
    CONTACT("contact", ContactMessageNode.CODEC),       // 推荐好友/群
    LOCATION("location", LocationMessageNode.CODEC),    // 位置
    MUSIC("music", MusicMessageNode.CODEC),             // 音乐分享
    REPLY("reply", ReplyMessageNode.CODEC),             // 回复
    FORWARD("forward", ForwardMessageNode.CODEC),       // 合并转发
    NODE("node", NodeMessageNode.CODEC),                // 合并转发节点
    XML("xml", XMLMessageNode.CODEC),                   // XML消息
    JSON("json", JsonMessageNode.CODEC),                // json消息
    ;

    public static final Codec<MessageNodeType> CODEC = IStrSerializable.fromEnum(MessageNodeType.class);
    private final String type;
    private final Codec<? extends MessageNode> codec;

    @Override
    public String getSerializedName() {
        return this.type;
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
