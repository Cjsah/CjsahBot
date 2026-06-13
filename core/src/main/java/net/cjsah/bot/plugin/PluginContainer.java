package net.cjsah.bot.plugin;

import lombok.Data;
import net.cjsah.bot.loader.PluginClassLoader;

import java.nio.file.Path;

@Data
public class PluginContainer {
    private final PluginMetadata metadata;
    private final Path path;
    private final PluginEntrypoint entrypoint;
    private final PluginClassLoader loader;
}
