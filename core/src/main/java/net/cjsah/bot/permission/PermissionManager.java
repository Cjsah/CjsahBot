package net.cjsah.bot.permission;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

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

    public PermissionContext createContext(Function<Permissions, PermissionContext> factory) {
        return factory.apply(this.permissions);
    }

    public static void init() {
    }
}
