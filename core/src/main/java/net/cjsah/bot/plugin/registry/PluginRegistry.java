package net.cjsah.bot.plugin.registry;

public interface PluginRegistry {

    PluginCommandRegistry command();

    PluginEventRegistry event();

    PluginPermissionRegistry permission();

    PluginExecutorRegistry executor();

}
