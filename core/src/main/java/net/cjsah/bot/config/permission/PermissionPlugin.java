package net.cjsah.bot.config.permission;

import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.util.DataUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PermissionPlugin {
    public static final Codec<PermissionPlugin> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("defaultEnabled").forGetter(PermissionPlugin::defaultEnabled),
        OverrideRoleUser.CODEC.listOf().fieldOf("users").forGetter(PermissionPlugin::users),
        RoledGroup.CODEC.listOf().fieldOf("groups").forGetter(PermissionPlugin::groups),
        OverrideUserInGroup.CODEC.listOf().fieldOf("userInGroups").forGetter(PermissionPlugin::userInGroups)
    ).apply(instance, PermissionPlugin::new));

    public static final Codec<Map<String, PermissionPlugin>> PLUGINS_CODEC = Codec.unboundedMap(Codec.STRING, CODEC);
    public static final PermissionPlugin EMPTY = new PermissionPlugin(true, List.of(), List.of(), List.of());

    private final boolean defaultEnabled;
    private final Map<Long, OverrideRoleUser> users;
    private final Map<Long, RoledGroup> groups;
    private final Table<Long, Long, OverrideUserInGroup> userInGroups;

    public PermissionPlugin(boolean defaultEnabled, List<OverrideRoleUser> users, List<RoledGroup> groups, List<OverrideUserInGroup> userInGroups) {
        this.defaultEnabled = defaultEnabled;
        this.users = DataUtil.makeMap(users);
        this.groups = DataUtil.makeMap(groups);
        this.userInGroups = DataUtil.makeTable(userInGroups);
    }

    public Optional<OverrideRoleUser> getUser(long id) {
        return Optional.ofNullable(this.users.get(id));
    }

    public Optional<RoledGroup> getGroup(long id) {
        return Optional.ofNullable(this.groups.get(id));
    }

    public Optional<OverrideUserInGroup> getUserInGroup(long userId, long groupId) {
        return Optional.ofNullable(this.userInGroups.get(groupId, userId));
    }

    public boolean defaultEnabled() {
        return this.defaultEnabled;
    }

    public List<OverrideRoleUser> users() {
        return DataUtil.unpack(this.users);
    }

    public List<RoledGroup> groups() {
        return DataUtil.unpack(this.groups);
    }

    public List<OverrideUserInGroup> userInGroups() {
        return DataUtil.unpack(this.userInGroups);
    }
}
