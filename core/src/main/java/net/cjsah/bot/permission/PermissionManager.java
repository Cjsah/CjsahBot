package net.cjsah.bot.permission;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.FilePaths;
import net.cjsah.bot.command.context.CommandNode;
import net.cjsah.bot.data.ChannelInfo;
import net.cjsah.bot.data.RoleInfo;
import net.cjsah.bot.data.RoomInfo;
import net.cjsah.bot.data.UserInfo;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.permission.role.PermissionNode;
import net.cjsah.bot.util.JsonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PermissionManager {
    private static final List<PermissionNode> global = new ArrayList<>();
    private static final Map<String, List<PermissionNode>> plugins = new LinkedHashMap<>();
    private static final Map<String, List<PermissionNode>> commands = new LinkedHashMap<>();

    public static boolean hasPermission(UserInfo user, HeyboxPermission[] permissions) {
        return PermissionManager.hasHeyboxPermission(user.getRoles(), permissions);
    }

    public static boolean hasCommandPermission(CommandEvent msg, CommandNode command) {
        UserInfo sender = msg.getSenderInfo();
        RoomInfo room = msg.getRoomInfo();
        ChannelInfo channel = msg.getChannelInfo();

        return PermissionManager.hasHeyboxPermission(sender.getRoles(), command.getPermissions()) &&
                PermissionManager.hasConsolePermission(sender.getId(), room.getId(), channel.getId(), command.getPluginId(), command.getName(), command.getRole());
    }

    public static boolean hasHeyboxPermission(List<RoleInfo> roles, HeyboxPermission[] permissions) {
        role:
        for (RoleInfo role : roles) {
            long rolePermission = role.getPermissions();
            if (rolePermission == 1L) return true;
            for (HeyboxPermission permission : permissions) {
                if ((rolePermission & permission.getValue()) == 0) {
                    continue role;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean hasConsolePermission(long userId, String roomId, String channelId, String pluginId, String command, PermissionRole role) {
        PermissionRoleNode validator = new PermissionRoleNode();
        if (matchRole(validator, global, roomId, channelId, userId)) {
            return false;
        }
        List<PermissionNode> nodes = plugins.get(pluginId);
        if (nodes != null && !nodes.isEmpty() && matchRole(validator, nodes, roomId, channelId, userId)) {
            return false;
        }
        nodes = commands.get(command);
        if (nodes != null && !nodes.isEmpty() && matchRole(validator, nodes, roomId, channelId, userId)) {
            return false;
        }
        return validator.getRole().getLevel() >= role.getLevel();
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
    public static void init() {
        global.clear();
        plugins.clear();
        commands.clear();
        String content = FilePaths.PERMISSION.read();
        JSONObject json = JsonUtil.deserialize(content);
        global.addAll(PermissionManager.parseNodes(json, "global"));
        PermissionManager.parseArray(json, "plugins", plugins);
        PermissionManager.parseArray(json, "commands", commands);
    }

    private static void parseArray(JSONObject root, String key, Map<String, List<PermissionNode>> map) {
        for (JSONObject json : root.getList(key, JSONObject.class)) {
            String id = json.getString("id");
            List<PermissionNode> permissions = PermissionManager.parseNodes(json, "permissions");
            map.put(id, permissions);
        }
    }

    private static List<PermissionNode> parseNodes(JSONObject root, String key) {
        List<JSONObject> array = root.getList(key, JSONObject.class);
        List<PermissionNode> nodes = array.stream().map(PermissionNodeType::generate).toList();
        List<PermissionNodeType> types = nodes.stream().map(PermissionNode::getType).distinct().toList();
        PermissionNode conflict = nodes.stream().filter(it -> it.isConflict(types)).findFirst().orElse(null);
        if (conflict != null) {
            throw BuiltExceptions.CONFLICT_PERMISSION.create(conflict.getType().getTitle());
        }
        return nodes;
    }
}
