package net.cjsah.bot.plugin;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public final class PluginContextDep {
    private static final Logger log = LoggerFactory.getLogger("PluginContext");
    static final ThreadLocal<PluginDep> PLUGIN = new ThreadLocal<>();
    static final ThreadLocal<PluginInfoDep> PLUGIN_INFO = new ThreadLocal<>();

    static final Map<String, PluginData> PLUGINS = new ConcurrentHashMap<>();

    static void appendPlugin(PluginDep plugin, PluginInfoDep info, PluginLoader loader) {
        PluginData data = new PluginData(plugin, info, loader);
        PLUGINS.put(info.getId(), data);
    }

    static PluginData removePlugin(String pluginId) {
        return PluginContextDep.PLUGINS.remove(pluginId);
    }

    public static PluginDep getPlugin(String id) {
        return PLUGINS.get(id).plugin;
    }

    public static PluginDep getCurrentPlugin() {
        return getCurrentPlugin(true);
    }

    public static PluginDep getCurrentPluginOrThrow() throws CommandException {
        PluginDep plugin = PLUGIN.get();
        if (plugin == null) {
            throw BuiltExceptions.REGISTER_IN_PLUGIN.create();
        }
        return plugin;
    }

    public static PluginDep getCurrentPlugin(boolean warn) {
        PluginDep plugin = PLUGIN.get();
        if (plugin == null && warn) {
            log.warn("Unknown thread in plugin context");
        }
        return plugin;
    }

    public static PluginInfoDep getCurrentPluginInfo() {
        PluginInfoDep info = PLUGIN_INFO.get();
        if (info == null) {
            log.warn("Unknown thread in plugin context");
        }
        return info;
    }

    public static PluginInfoDep getPluginInfo(String pluginId) {
        PluginData data = PLUGINS.get(pluginId);
        if (data == null) {
            log.warn("Plugin info not found");
            return null;
        }
        return data.info;
    }

    public record PluginData(PluginDep plugin, PluginInfoDep info, PluginLoader loader) {}
}
