package net.cjsah.bot.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginContext {
    static final Map<String, PluginContainer> PLUGINS = new ConcurrentHashMap<>();
    private static final ThreadLocal<PluginContainer> PLUGIN = new ThreadLocal<>();

}
