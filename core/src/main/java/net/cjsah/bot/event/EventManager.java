package net.cjsah.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContext;
import net.cjsah.bot.plugin.PluginThreadPools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public final class EventManager {
    private static final List<EventNode<?>> events = new ArrayList<>();

    public static <T extends Event> void subscribe(Class<T> clazz, Consumer<T> handler) {
        Plugin plugin = PluginContext.getCurrentPlugin();
        subscribe(plugin, clazz, handler);
    }

    public static <T extends Event> void subscribe(Plugin plugin, Class<T> clazz, Consumer<T> handler) {
        events.add(new EventNode<>(plugin, clazz, handler));
    }

    public static void unsubscribe(Plugin plugin) {
        events.removeIf(it -> it.plugin == plugin);
    }

    public static <T extends Event> void unsubscribe(Class<T> event) {
        Plugin plugin = PluginContext.getCurrentPlugin();
        events.removeIf(it -> it.plugin == plugin && it.event == event);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> void broadcast(@Nullable T event) {
        if (event == null) return;
        events.stream().parallel().filter(it -> it.event.isAssignableFrom(event.getClass())).forEach(it -> {
            PluginThreadPools.execute(it.plugin, () -> {
                try {
                    ((Consumer<T>)it.handler).accept(event);
                } catch (Exception e) {
                    log.error("Error while handling event", e);
                }
            });
        });
    }

    record EventNode<T extends Event>(Plugin plugin, Class<T> event, Consumer<T> handler) {}

}
