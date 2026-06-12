package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HeartbeatStatus(boolean online, boolean good) {
    public static final Codec<HeartbeatStatus> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("online", false).forGetter(HeartbeatStatus::online),
        Codec.BOOL.fieldOf("good").forGetter(HeartbeatStatus::good)
    ).apply(instance, HeartbeatStatus::new));
}
