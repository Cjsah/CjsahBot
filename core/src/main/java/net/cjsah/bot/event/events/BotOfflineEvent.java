package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BotOfflineEvent extends ReceivedEvent {
    public static final Codec<BotOfflineEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(BotOfflineEvent::getUserId),
        Codec.STRING.fieldOf("tag").forGetter(BotOfflineEvent::getTag),
        Codec.STRING.fieldOf("message").forGetter(BotOfflineEvent::getMessage)
    ).apply(instance, BotOfflineEvent::new));

    private final long userId;
    private final String tag;
    private final String message;
}
