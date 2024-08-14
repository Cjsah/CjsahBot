package net.cjsah.bot.permission;

import net.cjsah.bot.permission.type.PermissionNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {
    private static final List<PermissionNode> global = new ArrayList<>();
    private static final Map<String, List<PermissionNode>> permissions = new LinkedHashMap<>();

    public static boolean gasPermission(String pluginId, long groupId, long userId) {
        for (PermissionNode node : global) {

        }


        return true;
    }
}
