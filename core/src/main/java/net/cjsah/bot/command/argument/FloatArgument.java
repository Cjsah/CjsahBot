package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record FloatArgument(float min, float max) implements Argument<Float> {

    public static FloatArgument floatArg() {
        return floatArg(Float.MIN_VALUE);
    }

    public static FloatArgument floatArg(float min) {
        return floatArg(min, Float.MAX_VALUE);
    }

    public static FloatArgument floatArg(float min, float max) {
        return new FloatArgument(min, max);
    }

    @Override
    public Float parse(final StringReader reader) throws CommandException {
        return reader.readFloat();
    }
}
