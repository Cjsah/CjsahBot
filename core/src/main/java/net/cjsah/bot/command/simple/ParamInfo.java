package net.cjsah.bot.command.simple;

import net.cjsah.bot.command.context.CommandContext;

import java.util.function.Function;

public record ParamInfo (Class<?> type, Function<CommandContext, Object> factory) {
    public static Function<CommandContext, Object> self() {
        return context -> context;
    }

    public static Function<CommandContext, Object> arg(String name, Class<?> type) {
        return context -> context.getArgument(name, type).orElse(null);
    }

    public static Function<CommandContext, Object> empty() {
        return context -> null;
    }
}
