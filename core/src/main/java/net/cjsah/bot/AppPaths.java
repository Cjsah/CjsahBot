package net.cjsah.bot;

import net.cjsah.bot.exception.AppException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppPaths {
    public static final Path CONTAINER = Paths.get(".").toAbsolutePath();
    public static final Path PLUGINS = CONTAINER.resolve("plugins");
    public static final Path CONFIG = CONTAINER.resolve("config");
    public static final Path APPLICATION = CONTAINER.resolve("application.json");
    public static final Path PERMISSIONS = CONTAINER.resolve("permissions.json");

    protected static void init() {
        try {
            if (!Files.isDirectory(PLUGINS)) {
                Files.createDirectories(PLUGINS);
            }
            if (!Files.isDirectory(CONFIG)) {
                Files.createDirectories(CONFIG);
            }
        } catch (IOException e) {
            throw new AppException("Failed to init AppPaths", e);
        }
    }
}
