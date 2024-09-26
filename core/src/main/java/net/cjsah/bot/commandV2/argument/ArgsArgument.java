package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.CommandException;

import java.util.Map;

public class ArgsArgument implements Argument<Map<String, String>> { // special
    public static ArgsArgument INSTANCE = new ArgsArgument();

    private ArgsArgument() {}

    @Override
    public Map<String, String> parse(final String node) throws CommandException {
        return null;
    }
}
