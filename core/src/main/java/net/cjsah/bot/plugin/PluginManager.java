package net.cjsah.bot.plugin;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.AppPaths;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.loader.PluginClassLoader;
import net.cjsah.bot.plugin.builtin.CorePlugin;
import net.cjsah.bot.plugin.registry.PluginRegistry;
import net.cjsah.bot.plugin.registry.PluginRegistryImpl;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j(topic = "PluginManager")
public class PluginManager {
    private static final Map<String, PluginContainer> PLUGINS = new ConcurrentHashMap<>();
    private static final Map<String, PluginExecutor> EXECUTORS = new ConcurrentHashMap<>();
    private static final ScopedValue<PluginContainer> CURRENT = ScopedValue.newInstance();

    public static void registry() throws InterruptedException {
        List<Path> plugins = getPluginJars();
        int size = plugins.size() + 1;
        log.info("共发现 {} 个插件", size);

        List<PluginContainer> containers = new ArrayList<>(size);
        containers.add(CorePlugin.INSTANCE);

        for (Path path : plugins) {
            PluginContainer container = PluginClassLoader.plugin(path);

            if (container == null) {
                continue;
            }

            containers.add(container);
        }

        CountDownLatch latch = new CountDownLatch(containers.size());
        Consumer<Plugin> countdown = _ -> latch.countDown();

        long success = containers.stream()
            .filter(plugin -> PluginManager.register(plugin, countdown))
            .count();

        latch.await();

        log.info("成功加载 {} 个插件", success);
    }

    public static void halt(boolean await) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(PLUGINS.size());
        Consumer<PluginContainer> countdown = _ -> latch.countDown();

        for (PluginContainer plugin : PLUGINS.values()) {
            deregister(plugin.id(), countdown);
        }
        if (await) {
            latch.await();
        }
    }

    public static boolean register(PluginContainer plugin) {
        return register(plugin, _ -> {});
    }

    public static boolean register(PluginContainer plugin, Consumer<Plugin> fallback) {
        String id = plugin.id();
        if (PLUGINS.containsKey(id)) {
            PluginClassLoader.log.warn("Plugin {} has already registered.", id);
            fallback.accept(null);
            return false;
        }
        PluginExecutor executor = new PluginExecutor(CURRENT, plugin);
        PLUGINS.put(id, plugin);
        EXECUTORS.put(id, executor);
        execute(id, invokePluginFallback(id, Plugin::load, fallback));
        return true;
    }

    public static void deregister(String pluginId) {
        deregister(pluginId, _ -> {});
    }

    public static void deregister(String pluginId, Consumer<PluginContainer> fallback) {
        if (CorePlugin.INSTANCE.id().equals(pluginId)) {
            PluginClassLoader.log.warn("Core plugin cannot be uninstalled");
            fallback.accept(CorePlugin.INSTANCE);
            return;
        }
        if (!PLUGINS.containsKey(pluginId)) {
            PluginClassLoader.log.warn("Plugin {} is not exist.", pluginId);
            fallback.accept(null);
            return;
        }
        EventManager.getInstance().unsubscribe(pluginId);
        Commands.deregisterPlugin(pluginId);
        execute(pluginId, invokePlugin(pluginId, Plugin::unload));
        PluginContainer plugin = PLUGINS.remove(pluginId);
        PluginExecutor executor = EXECUTORS.remove(pluginId);
        executor.shutdown(true);
        fallback.accept(plugin);
        plugin.loader().close();
    }

    public static boolean execute(String pluginId, Runnable task) {
        PluginExecutor executor = EXECUTORS.get(pluginId);
        if (executor == null) return false;
        return executor.submit(task);
    }

    @Nullable
    public static PluginContainer getPlugin(String pluginId) {
        return PLUGINS.get(pluginId);
    }

    public static boolean isPluginLoaded(String pluginId) {
        return PLUGINS.containsKey(pluginId);
    }

    public static Collection<PluginContainer> getPlugins() {
        return PLUGINS.values();
    }

    public static void checkExist(String pluginId) {
        if (!isPluginLoaded(pluginId)) {
            throw BuiltinExceptions.PLUGIN_NOT_FOUND.create(pluginId);
        }
    }

    public static PluginContainer getCurrent() {
        PluginContainer current = CURRENT.get();
        if (current == null) {
            throw BuiltinExceptions.NOT_IN_PLUGIN.create();
        }
        return current;
    }

    public static PluginMetadata getCurrentInfo() {
        return getCurrent().metadata();
    }

    public static PluginRegistry getRegistry() {
        return new PluginRegistryImpl(getCurrent().metadata());
    }

    public static PluginExecutor getExecutor(String pluginId) {
        return EXECUTORS.get(pluginId);
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

    private static Runnable invokePluginFallback(String id, Consumer<Plugin> runnable, Consumer<Plugin> fallback) {
        return () -> {
            Optional<Plugin> instance = getPluginInstance(id);
            if (instance.isEmpty()) {
                fallback.accept(null);
                return;
            }
            Plugin plugin = instance.get();
            try {
                runnable.accept(plugin);
            } finally {
                fallback.accept(plugin);
            }
        };
    }

    private static List<Path> getPluginJars() {
        Path path = AppPaths.PLUGINS;

        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                return stream.filter(PluginManager::isValidPluginFile).map(Path::toAbsolutePath).toList();
            } catch (IOException e) {
                log.warn("Failed to load plugin from folder {}", path, e);
            }
        }

        return List.of();
    }

    public static boolean isValidPluginFile(Path path) {
        if (!Files.isRegularFile(path)) return false;
        String name = path.getFileName().toString();
        int index = name.lastIndexOf(".");
        return index != -1 && "jar".equalsIgnoreCase(name.substring(index + 1));
    }

}
