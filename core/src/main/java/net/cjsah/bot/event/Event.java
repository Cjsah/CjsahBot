package net.cjsah.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.resolver.ThreadPools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public final class Event {
    private static final Map<Class<? extends IEvent>, List<Consumer<IEvent>>> events = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends IEvent> void subscribe(Class<T> clazz, Consumer<T> handler) {
        List<Consumer<IEvent>> handlers = events.computeIfAbsent(clazz, k -> new ArrayList<>());
        handlers.add((Consumer<IEvent>) handler);
    }

    public static <T extends IEvent> void broadcast(@Nullable T event) {
        if (event == null) return;
        for (Map.Entry<Class<? extends IEvent>, List<Consumer<IEvent>>> entry : events.entrySet()) {
            if (entry.getKey().isAssignableFrom(event.getClass())) {
                for (Consumer<IEvent> handler : entry.getValue()) {
                    ThreadPools.execute(() -> {
                        try {
                            handler.accept(event);
                        } catch (Exception e) {
                            log.error("Error while handling event", e);
                        }
                    });
                }
            }
        }
    }

     record EventNode(Plugin plugin, Class<? extends IEvent> event, Consumer<IEvent> handler) {}

}
