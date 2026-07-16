package net.cjsah.bot.plugin.registry;

import net.cjsah.bot.plugin.PluginExecutor;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.PluginScheduledExecutorServiceImpl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class PluginExecutorRegistry {
    private final PluginExecutor executor;

    public PluginExecutorRegistry(PluginMetadata pluginInfo) {
        this.executor = PluginManager.getExecutor(pluginInfo.id());
    }

    public ScheduledExecutorService register(Supplier<ScheduledExecutorService> factory) {
        return new PluginScheduledExecutorServiceImpl(factory.get(), this.executor::getCarrier);
    }
}
