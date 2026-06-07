package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record PermissionPlugin(boolean defaultEnabled, List<OverrideRoleUser> users, List<RoledGroup> groups, List<OverrideUserInGroup> userInGroups) {
    public static final Codec<PermissionPlugin> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("defaultEnabled").forGetter(PermissionPlugin::defaultEnabled),
        OverrideRoleUser.CODEC.listOf().fieldOf("users").forGetter(PermissionPlugin::users),
        RoledGroup.CODEC.listOf().fieldOf("groups").forGetter(PermissionPlugin::groups),
        OverrideUserInGroup.CODEC.listOf().fieldOf("userInGroups").forGetter(PermissionPlugin::userInGroups)
    ).apply(instance, PermissionPlugin::new));

    public static final Codec<Map<String, PermissionPlugin>> PLUGINS_CODEC = Codec.unboundedMap(Codec.STRING, CODEC);
}
