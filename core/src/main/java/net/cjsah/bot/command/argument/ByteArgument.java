package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record ByteArgument(byte min, byte max) implements Argument<Byte> {

    public static ByteArgument byteArg() {
        return byteArg(Byte.MIN_VALUE);
    }

    public static ByteArgument byteArg(byte min) {
        return byteArg(min, Byte.MAX_VALUE);
    }

    public static ByteArgument byteArg(byte min, byte max) {
        return new ByteArgument(min, max);
    }

    @Override
    public Byte parse(final StringReader reader) throws CommandException {
        return reader.readByte();
    }
}
