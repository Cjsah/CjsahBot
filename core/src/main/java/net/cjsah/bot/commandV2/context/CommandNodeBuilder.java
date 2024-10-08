package net.cjsah.bot.commandV2.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNodeBuilder {
    private String name;
    private List<CommandParameter> parameters;
    private Method method;
    private String pluginId;

    public CommandNodeBuilder(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
    }

    public void appendParameter(CommandParameter parameter) {
        this.parameters.add(parameter);
    }

    public void setPlugin(String pluginId) {
        this.pluginId = pluginId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public CommandNode build() {
        return new CommandNode(this.name, this.method, Collections.unmodifiableList(this.parameters), this.pluginId);
    }
}
