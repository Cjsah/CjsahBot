package net.cjsah.bot.plugin.registry;

import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class PluginExecutorRegistry {
    private final ScopedValue.Carrier carrier;

    public PluginExecutorRegistry(PluginMetadata pluginInfo) {
        this.carrier = PluginManager.getExecutor(pluginInfo.id()).getCarrier();
    }

    public ScheduledExecutorService register(Function<ThreadFactory, ScheduledExecutorService> factory) {
        ThreadFactory threadFactory = runnable -> {
            Runnable wrapped = () -> this.carrier.run(runnable);
            return Thread.ofVirtual().unstarted(wrapped);
        };
        return factory.apply(threadFactory);
    }
}
