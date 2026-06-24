package net.cjsah.bot.registry;

import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.plugin.PluginMetadata;

import java.util.List;
import java.util.function.Consumer;

public class PluginEventRegistry {
    private final PluginMetadata pluginInfo;
    private final EventManager eventManager;

    public PluginEventRegistry(PluginMetadata pluginInfo) {
        this.pluginInfo = pluginInfo;
        this.eventManager = EventManager.getInstance();
    }

    public <T extends Event> long subscribe(Class<T> clazz, Consumer<T> handler) {
        return this.eventManager.subscribe(this.pluginInfo.id(), clazz, handler);
    }

    public List<Long> subscribe(Object object) {
        return this.eventManager.register(this.pluginInfo.id(), object);
    }

    public <T extends Event> boolean unsubscribe(Class<T> clazz) {
        return this.eventManager.unsubscribe(this.pluginInfo.id(), clazz);
    }

    public boolean unsubscribe(long id) {
        return this.eventManager.unsubscribe(id);
    }

    public boolean unsubscribeAll() {
        return this.eventManager.unsubscribe(pluginInfo.id());
    }
}
