package net.cjsah.bot.command.context;

import java.lang.reflect.Method;
import java.util.List;

public class CommandNode {
    private final String name;
    private final Method method;
    private final List<CommandParameter> parameters;
    private final String pluginId;

    public CommandNode(String node, Method method, List<CommandParameter> parameters, String pluginId) {
        this.name = node;
        this.method = method;
        this.parameters = parameters;
        this.pluginId = pluginId;
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
}
