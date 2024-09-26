package net.cjsah.bot.commandV2.context;

import java.util.ArrayList;
import java.util.List;

public class CommandNode {
    private final String name;
    private final List<CommandParameter> parameters;

    public CommandNode(String node) {
        this.name = node;
        this.parameters = new ArrayList<>();
    }

    public void appendParameter(CommandParameter parameter) {
        this.parameters.add(parameter);
    }

    public String getName() {
        return this.name;
    }

    public List<CommandParameter> getParameters() {
        return this.parameters;
    }
}
