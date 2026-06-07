package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record OverrideUserInGroup(long userId, long groupId, Optional<UserRole> role, Optional<Boolean> enabled) {
    public static Codec<OverrideUserInGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("userId").forGetter(OverrideUserInGroup::userId),
        Codec.LONG.fieldOf("groupId").forGetter(OverrideUserInGroup::groupId),
        UserRole.CODEC.optionalFieldOf("role").forGetter(OverrideUserInGroup::role),
        Codec.BOOL.optionalFieldOf("enabled").forGetter(OverrideUserInGroup::enabled)
    ).apply(instance, OverrideUserInGroup::new));
}
