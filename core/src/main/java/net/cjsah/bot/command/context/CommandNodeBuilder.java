package net.cjsah.bot.command.context;

import net.cjsah.bot.permission.HeyboxPermission;

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

    public void setPermissions(HeyboxPermission[] permissions) {
        this.permissions = permissions;
    }

    public CommandNode build() {
        return new CommandNode(this.name, this.method, Collections.unmodifiableList(this.parameters), this.pluginId, this.permissions);
    }
}
