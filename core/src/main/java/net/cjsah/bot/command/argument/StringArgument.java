package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public record StringArgument(ArgType type) implements Argument<String> {

    public static StringArgument byArg(String param) {
        if (param == null) return word();
        return switch (param) {
            case "word", "single_word" -> word();
            case "string", "quotable" -> string();
            case "greedy" -> greedyString();
            default -> throw new IllegalArgumentException("Unsupported types");
        };
    }

    public static StringArgument word() {
        return new StringArgument(ArgType.SINGLE_WORD);
    }

    public static StringArgument string() {
        return new StringArgument(ArgType.QUOTABLE_PHRASE);
    }

    public static StringArgument greedyString() {
        return new StringArgument(ArgType.GREEDY_PHRASE);
    }

    public static Optional<String> get(CommandContext context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandException {
        return switch (type) {
            case SINGLE_WORD -> reader.readUnquotedString();
            case QUOTABLE_PHRASE -> reader.readString();
            case GREEDY_PHRASE -> {
                String remaining = reader.getRemaining();
                reader.setCursor(reader.getTotalLength());
                yield remaining;
            }
        };
    }

    public enum ArgType {
        SINGLE_WORD,
        QUOTABLE_PHRASE,
        GREEDY_PHRASE
    }

}
