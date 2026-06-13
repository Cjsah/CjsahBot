package net.cjsah.bot.plugin;

import net.cjsah.bot.AppPaths;
import net.cjsah.bot.loader.PluginClassLoader;
import net.cjsah.bot.resolver.CountdownLocker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class PluginManager {

    public static void init() {
        List<Path> plugins = getPluginJars();
        CountdownLocker locker = new CountdownLocker(plugins.size());



    }

    private static List<Path> getPluginJars() {
        Path path = AppPaths.PLUGINS;

        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                return stream.filter(PluginManager::isValidPluginFile).toList();
            } catch (IOException e) {
                PluginClassLoader.log.warn("Failed to load plugin from folder {}", path, e);
            }
        }

        return List.of();
    }

    public static boolean isValidPluginFile(Path path) {
        if (!Files.isRegularFile(path)) return false;
        String name = path.getFileName().toString();
        int index = name.lastIndexOf(".");
        return index != -1 && "jar".equalsIgnoreCase(name.substring(index + 1));
    }

}
