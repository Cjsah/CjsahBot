package net.cjsah.bot.command;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class CommandManager {
    private static final Logger log = LoggerFactory.getLogger("CommandManager");

    private static final Dispatcher dispatcher = new Dispatcher();

    public static LiteralArgumentBuilder literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<T> required(String literal, Argument<T> type) {
        return RequiredArgumentBuilder.argument(literal, type);
    }

    public static void register(LiteralArgumentBuilder command) {
        dispatcher.register(command);
    }

    public static Map<String, String> getHelp(CommandSource<?> source) {
        return dispatcher.getHelp(source);
    }

    public static void execute(String command, CommandSource<?> source) {
        try {
            dispatcher.execute(command, source);
        } catch (CommandException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unkown exception", e);
        }
    }

}
