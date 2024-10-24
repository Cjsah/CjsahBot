package net.cjsah.bot.permission;

import net.cjsah.bot.data.RoleInfo;
import net.cjsah.bot.data.UserInfo;

public final class PermissionManager {
    public static boolean hasPermission(UserInfo user, Permission[] permissions) {
        role:
        for (RoleInfo role : user.getRoles()) {
            long rolePermission = role.getPermissions();
            if (rolePermission == 1L) return true;
            for (Permission permission : permissions) {
                if ((rolePermission & permission.getValue()) == 0) {
                    continue role;
                }
            }
            return true;
        }
        return false;

    }
}
