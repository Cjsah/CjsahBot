package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record ByteArgument(byte min, byte max) implements Argument<Byte> {

    public static ByteArgument byteArg(String param) {
        if (param == null) return byteArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return byteArg(Byte.parseByte(param));
        }
        byte min = Byte.parseByte(param.substring(0, index));
        byte max = Byte.parseByte(param.substring(index + 1));
        return byteArg(min, max);
    }

    public static ByteArgument byteArg() {
        return byteArg(Byte.MIN_VALUE);
    }

    public static ByteArgument byteArg(byte min) {
        return byteArg(min, Byte.MAX_VALUE);
    }

    public static ByteArgument byteArg(byte min, byte max) {
        if (max < min) throw new IllegalArgumentException("The maximum value is less than the minimum");
        return new ByteArgument(min, max);
    }

    @Override
    public Byte parse(final StringReader reader) throws CommandException {
        return reader.readByte();
    }
}
