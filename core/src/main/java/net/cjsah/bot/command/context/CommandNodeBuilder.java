package net.cjsah.bot.command.context;

import net.cjsah.bot.permission.HeyboxPermission;
import net.cjsah.bot.permission.PermissionRole;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNodeBuilder {
    private String name;
    private final List<CommandParameter> parameters;
    private Method method;
    private String pluginId;
    private HeyboxPermission[] permissions;
    private PermissionRole role;

    public CommandNodeBuilder(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.role = PermissionRole.USER;
    }

    public CommandNodeBuilder appendParameter(CommandParameter parameter) {
        this.parameters.add(parameter);
        return this;
    }

    public CommandNodeBuilder setPlugin(String pluginId) {
        this.pluginId = pluginId;
        return this;
    }

    public CommandNodeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CommandNodeBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public CommandNodeBuilder setPermissions(HeyboxPermission[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public CommandNodeBuilder setRole(PermissionRole role) {
        this.role = role;
        return this;
    }

    public CommandNode build() {
        return new CommandNode(this.name, this.method, Collections.unmodifiableList(this.parameters), this.pluginId, this.permissions, this.role);
    }
}
