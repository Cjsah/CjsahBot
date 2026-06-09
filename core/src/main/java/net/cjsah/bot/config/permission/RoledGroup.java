package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.config.IRowMapper;

public record RoledGroup(long id, boolean enabled) implements IRowMapper {
    public static Codec<RoledGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("id").forGetter(RoledGroup::id),
        Codec.BOOL.fieldOf("enabled").forGetter(RoledGroup::enabled)
    ).apply(instance, RoledGroup::new));

    @Override
    public long getRowKey() {
        return this.id;
    }
}
