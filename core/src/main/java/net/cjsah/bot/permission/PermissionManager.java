package net.cjsah.bot.permission;

import net.cjsah.bot.config.Permissions;

public class PermissionManager {
    private static final PermissionManager INSTANCE = new PermissionManager();

    private Permissions permissions = Permissions.EMPTY;

    private PermissionManager() {}

    public void reload() {
        this.permissions = Permissions.loadOrCreate();
    }

    public static PermissionManager getInstance() {
        return INSTANCE;
    }


    public static void init() {
    }
}
