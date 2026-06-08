package net.cjsah.bot.command.context;

import net.cjsah.bot.command.CommandDispatcher;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandContextBuilder {
    private final Map<String, ParsedArgument<?>> arguments = new LinkedHashMap<>();
    private final CommandNode rootNode;
    private final List<ParsedCommandNode> nodes = new ArrayList<>();
    private final CommandDispatcher dispatcher;
    private CommandSource<?> source;
    private Command command;
    private StringRange range;

    public CommandContextBuilder(final CommandDispatcher dispatcher, final CommandSource<?> source, final CommandNode rootNode, final int start) {
        this.rootNode = rootNode;
        this.dispatcher = dispatcher;
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder withSource(final CommandSource<?> source) {
        this.source = source;
        return this;
    }

    public CommandSource<?> getSource() {
        return this.source;
    }

    public CommandNode getRootNode() {
        return this.rootNode;
    }

    public CommandContextBuilder withArgument(final String name, final ParsedArgument<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public Map<String, ParsedArgument<?>> getArguments() {
        return arguments;
    }

    public CommandContextBuilder withCommand(final Command command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder withNode(final CommandNode node, final StringRange range) {
        this.nodes.add(new ParsedCommandNode(node, range));
        this.range = StringRange.encompassing(this.range, range);
        return this;
    }

    public CommandContextBuilder copy() {
        final CommandContextBuilder copy = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.start());
        copy.command = this.command;
        copy.arguments.putAll(this.arguments);
        copy.nodes.addAll(this.nodes);
        copy.range = this.range;
        return copy;
    }

    public Command getCommand() {
        return this.command;
    }

    public List<ParsedCommandNode> getNodes() {
        return this.nodes;
    }

    public CommandContext build(final String input) {
        return new CommandContext(this.source, input, this.arguments, this.command, this.rootNode, this.nodes, this.range);
    }

    public CommandDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public StringRange getRange() {
        return this.range;
    }
}
