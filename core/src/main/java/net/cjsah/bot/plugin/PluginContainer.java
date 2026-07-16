package net.cjsah.bot.plugin;

import lombok.Data;
import lombok.experimental.Accessors;
import net.cjsah.bot.loader.CloseableClassLoader;
import net.cjsah.bot.plugin.entry.PluginEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.ThreadFactory;

@Data
@Accessors(fluent = true)
public class PluginContainer {
    private final PluginMetadata metadata;
    private final Path path;
    private final PluginEntryPoint entrypoint;
    private final CloseableClassLoader loader;
    private final Logger log;

    public PluginContainer(PluginMetadata metadata, Path path, PluginEntryPoint entrypoint, CloseableClassLoader loader) {
        this.metadata = metadata;
        this.path = path;
        this.entrypoint = entrypoint;
        this.loader = loader;
        this.log = LoggerFactory.getLogger(metadata.id());
    }

    public String id() {
        return this.metadata.id();
    }

    public ThreadFactory virtualThreadFactory(String prefix) {
        PluginExecutor executor = PluginManager.getExecutor(this.id());
        ScopedValue.Carrier carrier = executor.getCarrier();
        return runnable -> {
            Runnable wrapped = () -> carrier.run(runnable);
            return Thread.ofVirtual().name(prefix, 0)
                .unstarted(wrapped);
        };
    }

}
