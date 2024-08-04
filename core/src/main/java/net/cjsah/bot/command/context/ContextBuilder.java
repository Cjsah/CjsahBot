package net.cjsah.bot.command.context;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.Dispatcher;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContextBuilder {
    private final Dispatcher dispatcher;
    private final CommandSource<?> source;
    private final CommandNode rootNode;
    private final Map<String, ParsedNode<?>> arguments = new LinkedHashMap<>();
    private Command command = null;
    private Range range;

    public ContextBuilder(Dispatcher dispatcher, CommandSource<?> source, CommandNode rootNode, int start) {
        this.dispatcher = dispatcher;
        this.source = source;
        this.rootNode = rootNode;
        this.range = new Range(start);
    }

    public ContextBuilder withArgument(String name, ParsedNode<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public ContextBuilder withCommand(@Nullable Command command) {
        this.command = command;
        return this;
    }

    public ContextBuilder withRange(Range range) {
        this.range = range;
        return this;
    }

    public ContextBuilder copy() {
        ContextBuilder builder = new ContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.start);
        builder.command = this.command;
        builder.arguments.putAll(this.arguments);
        builder.range = this.range;
        return builder;
    }

    public CommandContext build() {
        return new CommandContext(this.arguments, this.source, this.command);
    }

    public CommandSource<?> getSource() {
        return this.source;
    }

    public Range getRange() {
        return this.range;
    }
}
