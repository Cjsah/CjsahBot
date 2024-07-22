package net.cjsah.bot.event;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Event {
    private static final Map<Class<? extends IEvent>, List<Consumer<IEvent>>> events = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends IEvent> void subscribe(Class<T> clazz, Consumer<T> handler) {
        List<Consumer<IEvent>> handlers = events.computeIfAbsent(clazz, k -> new ArrayList<>());
        handlers.add((Consumer<IEvent>) handler);
    }

    public static <T extends IEvent> void broadcast(@Nullable T event) {
        if (event == null) return;
        List<Consumer<IEvent>> handlers = events.get(event.getClass());
        if (handlers != null) {
            for (Consumer<IEvent> handler : handlers) {
                handler.accept(event);
            }
        }
    }

}
