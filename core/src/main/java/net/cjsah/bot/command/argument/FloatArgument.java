package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class FloatArgument implements Argument<Float> {
    private final float min;
    private final float max;

    public FloatArgument(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public static FloatArgument floatArg() {
        return new FloatArgument(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public static FloatArgument floatArg(float min) {
        return new FloatArgument(min, Float.MAX_VALUE);
    }

    public static FloatArgument floatArg(float min, float max) {
        return new FloatArgument(min, max);
    }

    public static float getFloat(CommandContext context, String name) {
        return context.getArgument(name, float.class);
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    @Override
    public Float parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        float result = reader.readFloat();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.FLOAT_TOO_LOW.create(result, this.min);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.FLOAT_TOO_HIGH.create(result, this.max);
        }
        return result;
    }
}
