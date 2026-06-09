package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.config.IRowMapper;

public record RoledUser(long id, UserRole role, boolean enabled) implements IRowMapper {
    public static Codec<RoledUser> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("id").forGetter(RoledUser::id),
        UserRole.CODEC.fieldOf("role").forGetter(RoledUser::role),
        Codec.BOOL.fieldOf("enabled").forGetter(RoledUser::enabled)
    ).apply(instance, RoledUser::new));

    @Override
    public long getRowKey() {
        return this.id;
    }
}
