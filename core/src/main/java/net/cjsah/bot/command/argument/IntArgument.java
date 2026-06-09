package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record IntArgument(int min, int max) implements Argument<Integer> {

    public static IntArgument intArg(String param) {
        if (param == null) return intArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return intArg(Integer.parseInt(param));
        }
        int min = Integer.parseInt(param.substring(0, index));
        int max = Integer.parseInt(param.substring(index + 1));
        return intArg(min, max);
    }

    public static IntArgument intArg() {
        return intArg(Integer.MIN_VALUE);
    }

    public static IntArgument intArg(int min) {
        return intArg(min, Integer.MAX_VALUE);
    }

    public static IntArgument intArg(int min, int max) {
        return new IntArgument(min, max);
    }

    @Override
    public Integer parse(final StringReader reader) throws CommandException {
        return reader.readInt();
    }
}
