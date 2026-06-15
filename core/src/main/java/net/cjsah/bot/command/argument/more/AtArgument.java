package net.cjsah.bot.command.argument.more;

import cn.hutool.core.lang.Pair;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public class AtArgument implements Argument<Long> {
    private static final String EXPECT = "at";

    public static AtArgument atArg() {
        return new AtArgument();
    }

    public static Optional<Long> get(CommandContext context, String name) {
        return context.getArgument(name, Long.class);
    }

    @Override
    public Long parse(final StringReader reader) throws CommandException {
        Pair<String, String> pair = reader.readPair();
        if (!EXPECT.equals(pair.getKey())) {
            throw BuiltinExceptions.NOT_EXPECTED.create(EXPECT, pair.getKey());
        }
        return Long.parseLong(pair.getValue());
    }
}
