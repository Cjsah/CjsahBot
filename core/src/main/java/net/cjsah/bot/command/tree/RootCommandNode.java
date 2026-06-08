package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.exception.CommandException;

public class RootCommandNode extends CommandNode {
    public RootCommandNode() {
        super(null, null, CommandManager.passRequirement());
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getUsageText() {
        return "";
    }

    @Override
    protected boolean isValidInput(String input) {
        return false;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder contextBuilder) throws CommandException {

    }

    @Override
    protected ArgumentBuilder<?> builderFactory() {
        return null;
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        throw new IllegalStateException("Cannot convert root into a builder");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RootCommandNode)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "<root>";
    }
}
