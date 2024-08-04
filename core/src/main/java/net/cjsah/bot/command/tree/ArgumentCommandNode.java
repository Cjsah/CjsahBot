package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.command.context.ParsedNode;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ArgumentCommandNode<T> extends CommandNode {
    private final String name;
    private final Argument<T> type;

    public ArgumentCommandNode(
            String help,
            String name,
            Argument<T> type,
            Predicate<CommandSource<?>> requirement,
            @Nullable Command command
    ) {
        super(help, requirement, command);
        this.name = name;
        this.type = type;
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
    protected String getSortedKey() {
        return this.name;
    }

    @Override
    protected boolean isValidInput(String input) {
        try {
            StringReader reader = new StringReader(input);
            this.type.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        } catch (CommandException ignored) {
            return false;
        }
    }

    @Override
    public void parse(StringReader reader, ContextBuilder builder) throws CommandException {
        int start = reader.getCursor();
        T result = this.type.parse(reader);
        ParsedNode<T> parsed = new ParsedNode<>(start, reader.getCursor(), result);
        builder.withArgument(this.name, parsed).withRange(parsed.getRange());
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        RequiredArgumentBuilder<T> builder = RequiredArgumentBuilder.argument(this.name, this.type);
        builder.requires(this.getRequirement());
        if (this.getCommand() != null) {
            builder.executes(this.getHelp(), this.getCommand());
        }
        return builder;
    }

}
