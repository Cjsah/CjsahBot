package net.cjsah.bot.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.exception.PluginException;
import net.cjsah.bot.plugin.PluginContainer;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.LoaderEntryPointImpl;
import net.cjsah.bot.util.CodecUtil;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j(topic = "PluginLoader", access = AccessLevel.PUBLIC)
public class PluginClassLoader extends URLClassLoader {
    private final PluginContainer container;

    private PluginClassLoader(Path path) throws Exception {
        super(new URL[]{path.toUri().toURL()});
        PluginMetadata metadata = this.readMetadata(path);
        LoaderEntryPointImpl entrypoint = new LoaderEntryPointImpl(metadata.entrypoint(), this);
        this.container = new PluginContainer(metadata, path, entrypoint, this);
    }

    @Nullable
    @SuppressWarnings("resource")
    public static PluginContainer plugin(Path path) {
        try {
            return new PluginClassLoader(path).getContainer();
        } catch (Exception e) {
            log.error("Failed to load plugin", e);
            return null;
        }
    }

    public PluginContainer getContainer() {
        return this.container;
    }

    private PluginMetadata readMetadata(Path path) throws Exception {
        try (JarFile jar = new JarFile(path.toFile())) {
            JarEntry entry = jar.getJarEntry("plugin.json");
            if (entry == null) {
                throw new PluginException("plugin.json not found in " + path);
            }
            try (InputStream is = jar.getInputStream(entry);
                 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)
            ) {
                JsonElement json = JsonParser.parseReader(reader);
                return CodecUtil.decode(PluginMetadata.CODEC, json).left()
                    .orElseThrow(() -> new PluginException("Failed to parse plugin.json"));
            }
        }
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception ignored) {
        }
    }
}
