package net.cjsah.bot.command.context;

import net.cjsah.bot.permission.RoleType;

import java.lang.reflect.Method;
import java.util.List;

public class CommandNode {
    private final String name;
    private final Method method;
    private final List<CommandParameter> parameters;
    private final String pluginId;
    private final RoleType role;

    public CommandNode(String node, Method method, List<CommandParameter> parameters, String pluginId, RoleType role) {
        this.name = node;
        this.method = method;
        this.parameters = parameters;
        this.pluginId = pluginId;
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

    public RoleType getRole() {
        return this.role;
    }
}
