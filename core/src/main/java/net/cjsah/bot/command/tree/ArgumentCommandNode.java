package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.context.ParsedArgument;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ArgumentCommandNode<T> extends CommandNode {
    private final String name;
    private final Argument<T> argument;

    public ArgumentCommandNode(String name, Argument<T> argument, @Nullable String pluginId, @Nullable Command command, Predicate<CommandSource<?>> requirement) {
        super(pluginId, command, requirement);
        this.name = name;
        this.argument = argument;
    }

    public Argument<T> getArgument() {
        return this.argument;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsageText() {
        return "<" + this.name + ">";
    }

    @Override
    protected boolean isValidInput(String input) {
        try {
            final StringReader reader = new StringReader(input);
            this.argument.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        } catch (final CommandException ignored) {
            return false;
        }
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder contextBuilder) throws CommandException {
        final int start = reader.getCursor();
        final T result = this.argument.parse(reader);
        final ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);

        contextBuilder.withArgument(this.name, parsed);
        contextBuilder.withNode(this, parsed.getRange());

    }

    @Override
    protected ArgumentBuilder<?> builderFactory() {
        return RequiredArgumentBuilder.argument(this.getPluginId(), this.name, this.argument);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ArgumentCommandNode<?> that)) return false;

        if (!this.name.equals(that.name)) return false;
        if (!this.argument.equals(that.argument)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + this.argument.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "<argument " + this.name + ":" + this.argument +">";
    }
}
