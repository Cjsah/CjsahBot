package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MessageEmojiLike(String emojiId, int count) {
    public static final Codec<MessageEmojiLike> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("emoji_id").forGetter(MessageEmojiLike::emojiId),
        Codec.INT.fieldOf("count").forGetter(MessageEmojiLike::count)
    ).apply(instance, MessageEmojiLike::new));
}
