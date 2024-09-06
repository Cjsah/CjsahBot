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

    public static boolean hasPermission(String pluginId, String roomId, String channelId, long userId, RoleType permission) {
        PermissionRoleNode role = new PermissionRoleNode();
        if (matchRole(role, global, roomId, channelId, userId)) {
            return false;
        }
        List<PermissionNode> nodes = permissions.get(pluginId);
        if (nodes != null && !nodes.isEmpty() && matchRole(role, nodes, roomId, channelId, userId)) {
            return false;
        }
        return role.getRole().getLevel() >= permission.getLevel();
    }

    private static boolean matchRole(PermissionRoleNode role, List<PermissionNode> permissions, String roomId, String channelId, long userId) {
        for (PermissionNode node : permissions) {
            if (node.match(roomId, channelId, userId)) {
                node.handle(role);
                if (role.isDeny()) return true;
            }
        }
        return false;
    }
}
