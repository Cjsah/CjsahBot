package net.cjsah.bot.command.context;

import net.cjsah.bot.command.CommandDispatcher;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.tree.CommandNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandContextBuilder<S> {
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
    private final CommandNode<S> rootNode;
    private final List<ParsedCommandNode<S>> nodes = new ArrayList<>();
    private final CommandDispatcher<S> dispatcher;
    private S source;
    private Command<S> command;
    private StringRange range;

    public CommandContextBuilder(final CommandDispatcher<S> dispatcher, final S source, final CommandNode<S> rootNode, final int start) {
        this.rootNode = rootNode;
        this.dispatcher = dispatcher;
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder<S> withSource(final S source) {
        this.source = source;
        return this;
    }

    public S getSource() {
        return this.source;
    }

    public CommandNode<S> getRootNode() {
        return this.rootNode;
    }

    public CommandContextBuilder<S> withArgument(final String name, final ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public Map<String, ParsedArgument<S, ?>> getArguments() {
        return arguments;
    }

    public CommandContextBuilder<S> withCommand(final Command<S> command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder<S> withNode(final CommandNode<S> node, final StringRange range) {
        this.nodes.add(new ParsedCommandNode<>(node, range));
        this.range = StringRange.encompassing(this.range, range);
        return this;
    }

    public CommandContextBuilder<S> copy() {
        final CommandContextBuilder<S> copy = new CommandContextBuilder<>(this.dispatcher, this.source, this.rootNode, this.range.start());
        copy.command = this.command;
        copy.arguments.putAll(this.arguments);
        copy.nodes.addAll(this.nodes);
        copy.range = this.range;
        return copy;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public List<ParsedCommandNode<S>> getNodes() {
        return this.nodes;
    }

    public CommandContext<S> build(final String input) {
        return new CommandContext<>(this.source, input, this.arguments, this.command, this.rootNode, this.nodes, this.range);
    }

    public CommandDispatcher<S> getDispatcher() {
        return this.dispatcher;
    }

    public StringRange getRange() {
        return this.range;
    }
}
