package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.exception.CommandException;

public class RootCommandNode extends CommandNode {

    public RootCommandNode() {
        super("", source -> true, null);
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
    public void parse(StringReader reader, ContextBuilder builder) throws CommandException {
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        throw new IllegalStateException("Root command node cannot be created");
    }

    @Override
    protected String getSortedKey() {
        return "";
    }
}
