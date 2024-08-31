package net.cjsah.bot.permission;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.FilePaths;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.permission.type.PermissionNode;
import net.cjsah.bot.util.JsonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {
    private static final List<PermissionNode> global = new ArrayList<>();
    private static final Map<String, List<PermissionNode>> permissions = new LinkedHashMap<>();

    public static void init() {
        global.clear();
        permissions.clear();
        String content = FilePaths.PERMISSION.read();
        JSONObject json = JsonUtil.deserialize(content);
        JSONArray array = json.getJSONArray("global");
        global.addAll(parseNodes(array));
        array = json.getJSONArray("plugins");
        for (int i = 0; i < array.size(); i++) {
            json = array.getJSONObject(i);
            String id = json.getString("id");
            JSONArray permissionNodes = json.getJSONArray("permissions");
            permissions.put(id, parseNodes(permissionNodes));
        }
    }

    private static List<PermissionNode> parseNodes(JSONArray array) {
        List<PermissionNode> nodes = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            PermissionNode node = PermissionNodeType.generate(json);
            PermissionNode conflict = nodes.stream().filter(it -> it.isConflict(node)).findFirst().orElse(null);
            if (conflict != null) {
                throw BuiltExceptions.CONFLICT_PERMISSION.apply(conflict.getType().getTitle(), node.getType().getTitle());
            }
            nodes.add(node);
        }
        return nodes;
    }

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
