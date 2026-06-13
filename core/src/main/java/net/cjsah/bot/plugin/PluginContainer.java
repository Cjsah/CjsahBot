package net.cjsah.bot.plugin;

import lombok.Data;
import lombok.experimental.Accessors;
import net.cjsah.bot.loader.PluginClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Data
@Accessors(fluent = true)
public class PluginContainer {
    private final PluginMetadata metadata;
    private final Path path;
    private final PluginEntrypoint entrypoint;
    private final PluginClassLoader loader;
    private final Logger log = LoggerFactory.getLogger(this.metadata.id());

    public String id() {
        return this.metadata.id();
    }
}
