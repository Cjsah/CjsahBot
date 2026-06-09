package net.cjsah.bot.command.source;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.PermissionRole;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.Collection;
import java.util.function.Function;

public abstract class CommandSource<T> {
    protected final T sender;
    protected final PermissionContext permission;

    public CommandSource(T sender) {
        this.sender = sender;
        this.permission = PermissionManager.getInstance().createContext(this.permissionFactory());
    }

    public abstract void sendFeedback(String message);

    protected abstract Function<Permissions, PermissionContext> permissionFactory();

    public boolean hasPermission(PermissionRole role) {
        return this.permission.hasPermission(role);
    }

    public boolean hasPermission(PermissionRole role, Collection<String> pluginIds) {
        return this.permission.hasPermission(role, pluginIds);
    }
}
