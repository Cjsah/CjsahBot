package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.command.context.Range;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class LiteralCommandNode extends CommandNode {
    private final String literal;

    public LiteralCommandNode(
            String help,
            String literal,
            Predicate<CommandSource<?>> requirement,
            @Nullable Command command
    ) {
        super(help, requirement, command);
        this.literal = literal;
    }

    @Override
    public String getName() {
        return this.literal;
    }

    @Override
    public String getUsageText() {
        return this.literal;
    }

    @Override
    protected String getSortedKey() {
        return this.literal;
    }

    @Override
    protected boolean isValidInput(String input) {
        return this.parse(new StringReader(input)) > -1;
    }

    @Override
    public void parse(StringReader reader, ContextBuilder builder) throws CommandException {
        int start = reader.getCursor();
        int end = this.parse(reader);
        if (end < 0) throw BuiltExceptions.LITERAL_INCORRECT.create(this.literal);
        builder.withRange(new Range(start, end));
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        LiteralArgumentBuilder literal = LiteralArgumentBuilder.literal(this.literal);
        literal.requires(this.getRequirement());
        if (this.getCommand() != null) {
            literal.executes(this.getHelp(), this.getCommand());
        }
        return literal;
    }

    private int parse(StringReader reader) {
        if (reader.canRead(this.literal.length())) {
            int start = reader.getCursor();
            int end = start + this.literal.length();
            if (reader.getString().substring(start, end).equals(this.literal)) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                } else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }
}
