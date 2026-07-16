package net.cjsah.bot.plugin.registry;

import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.PluginScheduledExecutorServiceImpl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class PluginExecutorRegistry {
    private final ScopedValue.Carrier carrier;

    public PluginExecutorRegistry(PluginMetadata pluginInfo) {
        this.carrier = PluginManager.getExecutor(pluginInfo.id()).getCarrier();
    }

    public ScheduledExecutorService register(Supplier<ScheduledExecutorService> factory) {
        return new PluginScheduledExecutorServiceImpl(factory.get(), this.carrier);
    }
}
