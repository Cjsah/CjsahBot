package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IRowMapper;

import java.util.Optional;

public record OverrideRoleUser(long id, Optional<UserRole> role, Optional<Boolean> enabled) implements IRowMapper {
    public static Codec<OverrideRoleUser> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("id").forGetter(OverrideRoleUser::id),
        UserRole.CODEC.optionalFieldOf("role").forGetter(OverrideRoleUser::role),
        Codec.BOOL.optionalFieldOf("enabled").forGetter(OverrideRoleUser::enabled)
    ).apply(instance, OverrideRoleUser::new));

    @Override
    public long getRowKey() {
        return this.id;
    }
}
