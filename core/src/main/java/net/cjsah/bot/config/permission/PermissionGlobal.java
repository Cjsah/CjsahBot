package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PermissionGlobal(List<RoledUser> users, List<RoledGroup> groups, List<OverrideUserInGroup> userInGroups) {
    public static final Codec<PermissionGlobal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RoledUser.CODEC.listOf().fieldOf("users").forGetter(PermissionGlobal::users),
        RoledGroup.CODEC.listOf().fieldOf("groups").forGetter(PermissionGlobal::groups),
        OverrideUserInGroup.CODEC.listOf().fieldOf("userInGroups").forGetter(PermissionGlobal::userInGroups)
    ).apply(instance, PermissionGlobal::new));

    public static PermissionGlobal EMPTY = new PermissionGlobal(List.of(), List.of(), List.of());
}
