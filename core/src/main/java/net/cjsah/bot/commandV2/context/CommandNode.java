package net.cjsah.bot.commandV2.context;

import java.util.ArrayList;
import java.util.List;

public class CommandNode {
    private final String node;
    private final List<CommandParameter> parameters;

    public CommandNode(String node) {
        this.node = node;
        this.parameters = new ArrayList<>();
    }

    public void appendParameter(CommandParameter parameter) {
        this.parameters.add(parameter);
    }
}
