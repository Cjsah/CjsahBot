package net.cjsah.bot.command.context;

import net.cjsah.bot.permission.HeyboxPermission;
import net.cjsah.bot.permission.PermissionRole;

import java.lang.reflect.Method;
import java.util.List;

public class CommandNode {
    private final String name;
    private final Method method;
    private final List<CommandParameter> parameters;
    private final String pluginId;
    private final HeyboxPermission[] permissions;
    private final PermissionRole role;

    public CommandNode(String node, Method method, List<CommandParameter> parameters, String pluginId, HeyboxPermission[] permissions, PermissionRole role) {
        this.name = node;
        this.method = method;
        this.parameters = parameters;
        this.pluginId = pluginId;
        this.permissions = permissions;
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public Method getMethod() {
        return this.method;
    }

    public List<CommandParameter> getParameters() {
        return this.parameters;
    }

    public String getPluginId() {
        return this.pluginId;
    }

    public HeyboxPermission[] getPermissions() {
        return this.permissions;
    }

    public PermissionRole getRole() {
        return this.role;
    }
}
