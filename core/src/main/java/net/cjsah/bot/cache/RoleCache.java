package net.cjsah.bot.cache;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.RoleInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class RoleCache {
    private static final Map<String, Map<String, RoleInfo>> RoleCaches = new HashMap<>();

    public static List<RoleInfo> getRole(String roomId, List<String> roleIds) {
        return RoleCache.getRole(roomId, false, roleIds);
    }

    public static List<RoleInfo> getRole(String roomId, boolean forceRefresh, List<String> roleIds) {
        Map<String, RoleInfo> roleCache = RoleCache.getRoomCache(roomId, forceRefresh);
        return roleIds.stream().map(roleCache::get).filter(Objects::nonNull).toList();
    }

    public static Map<String, RoleInfo> getRoomCache(String roomId) {
        return RoleCache.getRoomCache(roomId, false);
    }

    public static Map<String, RoleInfo> getRoomCache(String roomId, boolean forceRefresh) {
        Map<String, RoleInfo> roles = RoleCaches.get(roomId);
        if (roles == null || forceRefresh) {
            roles = Api.getRoomRoles(roomId).stream().collect(Collectors.toMap(RoleInfo::getId, it -> it));
            RoleCaches.put(roomId, roles);
        }
        return roles;
    }

    public static void clearCache() {
        RoleCaches.clear();
    }
}
