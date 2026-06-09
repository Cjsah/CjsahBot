package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record FloatArgument(float min, float max) implements Argument<Float> {

    public static FloatArgument floatArg(String param) {
        if (param == null) return floatArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return floatArg(Float.parseFloat(param));
        }
        float min = Float.parseFloat(param.substring(0, index));
        float max = Float.parseFloat(param.substring(index + 1));
        return floatArg(min, max);
    }

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
