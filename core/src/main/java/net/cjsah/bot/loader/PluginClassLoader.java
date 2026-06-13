package net.cjsah.bot.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.cjsah.bot.exception.PluginException;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContainer;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.util.CodecUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginClassLoader extends URLClassLoader {
    private final PluginContainer container;

    public PluginClassLoader(Path path) throws Exception {
        super(new URL[]{path.toUri().toURL()});

        PluginMetadata metadata = readMetadata(path);

        Class<?> clazz = this.loadClass(metadata.getEntrypoint());
        if (!Plugin.class.isAssignableFrom(clazz)) {
            throw new PluginException("Entrypoint " + metadata.getEntrypoint() + " does not extend Plugin");
        }

        Plugin entry = (Plugin) clazz.getDeclaredConstructor().newInstance();

        this.container = new PluginContainer(metadata, path, entry, this);
    }

    public PluginContainer getContainer() {
        return this.container;
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception ignored) {}
    }

    private static PluginMetadata readMetadata(Path path) throws Exception {
        try (JarFile jar = new JarFile(path.toFile())) {
            JarEntry entry = jar.getJarEntry("plugin.json");
            if (entry == null) {
                throw new PluginException("plugin.json not found in " + path);
            }
            try (InputStream is = jar.getInputStream(entry);
                 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                JsonElement json = JsonParser.parseReader(reader);
                return CodecUtil.decode(PluginMetadata.CODEC, json).left()
                    .orElseThrow(() -> new PluginException("Failed to parse plugin.json"));
            }
        }
    }
}
