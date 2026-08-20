package net.cjsah.bot.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.util.CodecUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

@Slf4j(topic = "ConfigManager")
public class ConfigNode<T> {
    private final String pluginId;
    private final Path path;
    private final Codec<T> codec;
    private final T defaultValue;
    private T value;

    public ConfigNode(String pluginId, Path path, Codec<T> codec, T defaultValue) {
        this.pluginId = pluginId;
        this.path = path.toAbsolutePath().normalize();
        this.codec = codec;
        this.defaultValue = defaultValue;
        this.loadOrCreate();
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T loadOrCreate() {
        try {
            this.checkPath();
            if (!Files.isRegularFile(this.path)) {
                this.value = this.defaultValue;
                String json = CodecUtil.encode(this.codec, this.value).orThrow();
                Files.writeString(this.path, json, StandardCharsets.UTF_8);
                return this.value;
            }
            String json = Files.readString(this.path, StandardCharsets.UTF_8);
            Either<T, String> either = CodecUtil.decode(this.codec, json);
            this.value = either.map(Function.identity(), _ -> this.defaultValue);
            either.ifRight(err -> {
                log.warn("Failed to load config, use default value: {}", err);
            });
            return this.value;
        } catch (Exception e) {
            log.warn("Failed to load config, use default value.", e);
            return this.value = this.defaultValue;
        }
    }

    public void save() {
        try {
            this.checkPath();
            String json = CodecUtil.encode(this.codec, this.value).orThrow();
            Files.writeString(this.path, json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("Failed to save config.", e);
        }
    }

    private void checkPath() throws IOException {
        Path parent = this.path.getParent();
        if (!Files.isDirectory(parent)) {
            Files.createDirectories(parent);
        }
    }
}
