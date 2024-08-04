package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContext;

public class PluginArgument implements Argument<Plugin> {

    public static PluginArgument plugin() {
        return new PluginArgument();
    }

    public static Plugin getPlugin(CommandContext context, String name) {
        return context.getArgument(name, Plugin.class);
    }

    @Override
    public Plugin parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        String id = reader.readUnquotedString();
        Plugin plugin = PluginContext.getPlugin(id);
        if (plugin == null) {
            reader.setCursor(start);
            throw BuiltExceptions.PLUGIN_NOT_FOUND.create(id);
        }
        return plugin;
    }
}
