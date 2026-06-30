package net.cjsah.bot.plugin.registry;

import cn.hutool.core.lang.loader.LazyFunLoader;
import cn.hutool.core.lang.loader.LazyLoader;
import lombok.Data;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.plugin.PluginMetadata;

import java.util.function.Function;

@Data
public class PluginRegistryImpl implements PluginRegistry {
    private final PluginMetadata metadata;
    private final LazyLoader<PluginCommandRegistry> pluginContext;
    private final LazyLoader<PluginEventRegistry> eventContext;
    private final LazyLoader<PluginPermissionRegistry> permissionContext;
    private final LazyLoader<PluginExecutorRegistry> executorContext;

    public PluginRegistryImpl(PluginMetadata metadata) {
        this.metadata = metadata;
        this.pluginContext = lazy(Commands::registerContext);
        this.eventContext = lazy(PluginEventRegistry::new);
        this.permissionContext = lazy(PluginPermissionRegistry::new);
        this.executorContext = lazy(PluginExecutorRegistry::new);
    }

    @Override
    public PluginCommandRegistry command() {
        return this.pluginContext.get();
    }

    @Override
    public PluginEventRegistry event() {
        return this.eventContext.get();
    }

    @Override
    public PluginPermissionRegistry permission() {
        return this.permissionContext.get();
    }

    @Override
    public PluginExecutorRegistry executor() {
        return this.executorContext.get();
    }

    public <T> LazyLoader<T> lazy(Function<PluginMetadata, T> factory) {
        return new LazyFunLoader<>(() -> factory.apply(this.metadata));
    }
}
