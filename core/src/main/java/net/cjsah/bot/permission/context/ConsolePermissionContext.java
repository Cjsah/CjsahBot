package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.permission.UserRole;

import java.util.Collection;

public class ConsolePermissionContext extends PermissionContext {

    @Override
    public boolean hasPermission(UserRole role) {
        return true;
    }

    @Override
    public boolean hasPermission(UserRole role, Collection<String> pluginIds) {
        return true;
    }

    @Override
    public boolean hasPermission(String pluginId) {
        return true;
    }
}
