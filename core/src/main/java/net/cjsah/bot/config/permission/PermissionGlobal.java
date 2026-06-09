package net.cjsah.bot.config.permission;

import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.util.DataUtil;

import java.util.List;
import java.util.Map;

@Getter
public class PermissionGlobal {
    public static final Codec<PermissionGlobal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RoledUser.CODEC.listOf().fieldOf("users").forGetter(PermissionGlobal::users),
        RoledGroup.CODEC.listOf().fieldOf("groups").forGetter(PermissionGlobal::groups),
        OverrideUserInGroup.CODEC.listOf().fieldOf("userInGroups").forGetter(PermissionGlobal::userInGroups)
    ).apply(instance, PermissionGlobal::new));

    public static PermissionGlobal EMPTY = new PermissionGlobal(List.of(), List.of(), List.of());

    private final Map<Long, RoledUser> users;
    private final Map<Long, RoledGroup> groups;
    private final Table<Long, Long, OverrideUserInGroup> userInGroups;

    public PermissionGlobal(List<RoledUser> users, List<RoledGroup> groups, List<OverrideUserInGroup> userInGroups) {
        this.users = DataUtil.makeMap(users);
        this.groups = DataUtil.makeMap(groups);
        this.userInGroups = DataUtil.makeTable(userInGroups);
    }

    public List<RoledUser> users() {
        return DataUtil.unpack(this.users);
    }

    public List<RoledGroup> groups() {
        return DataUtil.unpack(this.groups);
    }

    public List<OverrideUserInGroup> userInGroups() {
        return DataUtil.unpack(this.userInGroups);
    }
}
