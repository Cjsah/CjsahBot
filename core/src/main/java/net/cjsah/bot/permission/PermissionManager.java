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

    public static boolean gasPermission(String pluginId, long groupId, long userId) {
        RoleType groupRole = RoleType.DENY;
        RoleType userRole = RoleType.DENY;
        boolean isInGroup = 0L == groupId;
        for (PermissionNode node : global) {
            if (node.isUseInGroup() == isInGroup && node.getList().contains(node.isMatchGroup() ? groupId : userId)) {
                switch (node.getType()) {
                    case USER_SET -> {
                        RoleSetNode roleNode = (RoleSetNode) node;
                        if (roleNode.getRole().getLevel() > userRole.getLevel()) {
                            userRole = roleNode.getRole();
                        }
                    }
                    case WHITE_GROUP -> {
                        groupRole = ()
                    }
                }

            }





        }


        return true;
    }
}
