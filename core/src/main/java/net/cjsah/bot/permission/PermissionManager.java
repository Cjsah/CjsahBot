package net.cjsah.bot.permission;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.permission.context.PermissionContext;
import net.cjsah.bot.plugin.PluginManager;

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

    public boolean registerPluginPermission(String pluginId, PermissionPlugin permission) {
        PluginManager.checkExist(pluginId);
        PermissionPlugin current = this.permissions.plugins().get(pluginId);
        if (current != null) return false;
        this.permissions.plugins().put(pluginId, permission);
        Permissions.save(this.permissions);
        return true;
    }

    public static void init() {
    }
}
