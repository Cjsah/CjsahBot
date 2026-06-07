package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RoledGroup(long id, boolean enabled) {
    public static Codec<RoledGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("id").forGetter(RoledGroup::id),
        Codec.BOOL.fieldOf("enabled").forGetter(RoledGroup::enabled)
    ).apply(instance, RoledGroup::new));

}
