package net.cjsah.bot;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FilePaths {
    private static final Logger log = LoggerFactory.getLogger("FilePath");
    private static final List<Path> Paths = new ArrayList<>();
    private static final List<AppFile> Files = new ArrayList<>();

    public static final Path PLUGIN = regPath("plugins");
    public static final Path CONFIG = regPath("config");
    public static final AppFile ACCOUNT = regFile("account.json", "{\"token\":\"\"}");
    public static final AppFile PERMISSION = regFile(CONFIG.resolve("permission.json"), "{\"global\":[],\"plugins\":[],\"commands\":[]}");

    public static Path regPath(String path) {
        Path p = new File(path).toPath();
        Paths.add(p);
        return p;
    }

    public static AppFile regFile(String path, String defaultContent) {
        Path p = new File(path).toPath();
        return regFile(p, defaultContent);
    }

    public static AppFile regFile(Path path, String defaultContent) {
        AppFile file = new AppFile(path, defaultContent);
        Files.add(file);
        return file;
    }

    public static void init() {
        for (Path path : Paths) {
            File file = path.toFile();
            if (file.exists() && file.isDirectory()) continue;
            if (!file.mkdirs()) {
                log.error("Failed to create directory {}", file.getAbsolutePath());
            }
        }
        for (AppFile path : Files) {
            path.checkAndCreate();
        }
    }

    public record AppFile(Path path, String defaultContent) {

        public boolean checkAndCreate() {
            File file = this.path.toFile();
            file = new File(file.getAbsolutePath());
            if (file.exists() && file.isFile()) return true;
            File parent = file.toPath().getParent().toFile();
            if (!parent.exists() && !parent.isDirectory()) {
                if (!file.mkdirs()) {
                    log.error("Failed to create directory {}, Failed to create file {}", parent.getAbsolutePath(), file.getAbsolutePath());
                }
            }
            FileUtil.writeString(this.defaultContent, file, StandardCharsets.UTF_8);
            return false;
        }

        public String read() {
            if (this.checkAndCreate()) {
                return FileUtil.readString(this.path.toFile(), StandardCharsets.UTF_8);
            } else return this.defaultContent;
        }
    }


}
