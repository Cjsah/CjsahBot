package net.cjsah.bot.permission;

import net.cjsah.bot.permission.type.PermissionNode;
import net.cjsah.bot.permission.type.RoleSetNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {
    private static final List<PermissionNode> global = new ArrayList<>();
    private static final Map<String, List<PermissionNode>> permissions = new LinkedHashMap<>();

    public static boolean hasPermission(String pluginId, long groupId, long userId, RoleType permission) {
        PermissionRoleNode role = new PermissionRoleNode();
        if (matchRole(role, global, groupId, userId)) {
            return false;
        }
        List<PermissionNode> nodes = permissions.get(pluginId);
        if (nodes != null && !nodes.isEmpty() && matchRole(role, nodes, groupId, userId)) {
            return false;
        }
        return role.getRole().getLevel() >= permission.getLevel();
    }

    private static boolean matchRole(PermissionRoleNode role, List<PermissionNode> permissions, long groupId, long userId) {
        boolean isInGroup = 0L != groupId;
        for (PermissionNode node : permissions) {
            if ((isInGroup ? node.canUseInGroup() : node.canUseInUser()) && node.match(groupId, userId)) {
                node.handle(role);
                if (role.isDeny()) return true;
            }
        }
        return false;
    }
}
