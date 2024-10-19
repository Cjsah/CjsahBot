package net.cjsah.bot.plugin;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PluginContext {
    private static final Logger log = LoggerFactory.getLogger("PluginContext");
    static final ThreadLocal<Plugin> PLUGIN = new ThreadLocal<>();
    static final ThreadLocal<PluginInfo> PLUGIN_INFO = new ThreadLocal<>();

    static final Map<String, PluginData> PLUGINS = new ConcurrentHashMap<>();
    private static final Map<Plugin, PluginData> PLUGIN_MAP = new ConcurrentHashMap<>();

    static void appendPlugin(Plugin plugin, PluginInfo info, PluginLoader loader) {
        PluginData data = new PluginData(plugin, info, loader);
        PLUGINS.put(info.getId(), data);
        PLUGIN_MAP.put(plugin, data);
    }

    static PluginData removePlugin(Plugin plugin) {
        PluginData data = PluginContext.PLUGIN_MAP.remove(plugin);
        if (data == null) return null;
        PluginContext.PLUGINS.remove(data.info().getId());
        return data;
    }

    public static Plugin getPlugin(String id) {
        return PLUGINS.get(id).plugin;
    }

    public static Plugin getCurrentPlugin() {
        return getCurrentPlugin(true);
    }

    public static Plugin getCurrentPluginOrThrow() throws CommandException {
        Plugin plugin = PLUGIN.get();
        if (plugin == null) {
            throw BuiltExceptions.REGISTER_IN_PLUGIN.create();
        }
        return plugin;
    }

    public static Plugin getCurrentPlugin(boolean warn) {
        Plugin plugin = PLUGIN.get();
        if (plugin == null && warn) {
            log.warn("Unknown thread in plugin context");
        }
        return plugin;
    }

    public static PluginInfo getCurrentPluginInfo() {
        PluginInfo info = PLUGIN_INFO.get();
        if (info == null) {
            log.warn("Unknown thread in plugin context");
        }
        return info;
    }

    public static PluginInfo getPluginInfo(Plugin plugin) {
        PluginData data = PLUGIN_MAP.get(plugin);
        if (data == null) {
            log.warn("Plugin info not found");
            return null;
        }
        return data.info;
    }

    public record PluginData(Plugin plugin, PluginInfo info, PluginLoader loader) {}
}
