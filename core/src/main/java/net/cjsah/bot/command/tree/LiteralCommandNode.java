package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.context.StringRange;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class LiteralCommandNode<S> extends CommandNode<S> {
    private final String literal;

    public LiteralCommandNode(String literal, @Nullable String pluginId, @Nullable Command<S> command, Predicate<S> requirement) {
        super(pluginId, command, requirement);
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
    protected boolean isValidInput(String input) {
        return parse(new StringReader(input)) > -1;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandException {
        final int start = reader.getCursor();
        final int end = parse(reader);
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }

        throw BuiltExceptions.LITERAL_INCORRECT.create(this.literal);
    }

    private int parse(final StringReader reader) {
        final int start = reader.getCursor();
        if (reader.canRead(this.literal.length())) {
            final int end = start + this.literal.length();
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

    @Override
    protected ArgumentBuilder<S, ?> builderFactory() {
        return LiteralArgumentBuilder.literal(this.literal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LiteralCommandNode<?> that)) return false;

        if (!this.literal.equals(that.literal)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = this.literal.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "<literal " + this.literal + ">";
    }
}
