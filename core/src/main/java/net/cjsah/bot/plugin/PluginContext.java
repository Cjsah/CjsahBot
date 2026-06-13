package net.cjsah.bot.plugin;

import net.cjsah.bot.loader.PluginClassLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PluginContext {
    private static final Map<String, PluginContainer> PLUGINS = new ConcurrentHashMap<>();
    private static final Map<String, PluginExecutor> EXECUTORS = new ConcurrentHashMap<>();
    private static final ScopedValue<PluginContainer> CURRENT = ScopedValue.newInstance();

    public static void register(PluginContainer plugin) {
        String id = plugin.id();
        if (PLUGINS.containsKey(id)) {
            PluginClassLoader.log.warn("Plugin {} has already registered.", id);
            return;
        }
        PluginExecutor executor = new PluginExecutor(CURRENT, plugin);
        PLUGINS.put(id, plugin);
        EXECUTORS.put(id, executor);
        execute(id, invokePlugin(id, Plugin::load));
    }

    public static void deregister(String pluginId) {
        if (!PLUGINS.containsKey(pluginId)) {
            PluginClassLoader.log.warn("Plugin {} is not exist.", pluginId);
            return;
        }
        execute(pluginId, invokePlugin(pluginId, Plugin::unload));
        PluginContainer plugin = PLUGINS.remove(pluginId);
        PluginExecutor executor = EXECUTORS.remove(pluginId);
        executor.shutdown(true);
        plugin.loader().close();
    }

    public static void execute(String pluginId, Runnable task) {
        PluginExecutor executor = EXECUTORS.get(pluginId);
        if (executor != null) {
            executor.submit(task);
        }
    }

    @Nullable
    public static PluginContainer getPlugin(String pluginId) {
        return PLUGINS.get(pluginId);
    }

    @Nullable
    public static PluginContainer getCurrent() {
        return CURRENT.get();
    }

    private static Optional<Plugin> getPluginInstance(String pluginId) {
        return Optional
            .ofNullable(PLUGINS.get(pluginId))
            .map(it -> it.entrypoint().getOrCreate());
    }

    private static Runnable invokePlugin(String id, Consumer<Plugin> runnable) {
        return () -> {
            Optional<Plugin> instance = getPluginInstance(id);
            if (instance.isEmpty()) return;
            runnable.accept(instance.get());
        };
    }
}
