package net.cjsah.bot;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilePaths {
    public static final List<Path> Paths = new ArrayList<>();
    private static final Path PLUGIN = regPath("plugins");
    private static final Path CONFIG = regPath("config");

    public static Path regPath(String path) {
        Path p = new File(path).toPath();
        Paths.add(p);
        return p;
    }

    public static void init() {
        for (Path path : Paths) {
            File file = path.toFile();
            if (file.exists() && file.isDirectory()) continue;
            if (!file.mkdirs()) {
                log.error("Failed to create directory {}", file.getAbsolutePath());
            }
        }
    }
}
