package net.cjsah.bot.plugin.entry;

import net.cjsah.bot.plugin.Plugin;

public interface PluginEntryPoint {
    Plugin getOrCreate();
}
