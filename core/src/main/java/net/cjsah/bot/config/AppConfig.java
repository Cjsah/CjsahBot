package net.cjsah.bot.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.AppPaths;
import net.cjsah.bot.exception.AppException;
import net.cjsah.bot.util.CodecUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j(topic = "ConfigManager")
public record AppConfig(String url, String token) {
    public static final Codec<AppConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("url").forGetter(AppConfig::url),
        Codec.STRING.fieldOf("token").forGetter(AppConfig::token)
    ).apply(instance, AppConfig::new));

    public static final AppConfig EMPTY = new AppConfig("127.0.0.1", "");

    public static AppConfig loadOrCreate() {
        Path path = AppPaths.APPLICATION;
        try {
            if (!Files.isRegularFile(path)) {
                Files.createDirectories(path.getParent());
                AppConfig config = EMPTY;
                String json = CodecUtil.encode(CODEC, config, AppException::new).orThrow();
                Files.writeString(path, json, StandardCharsets.UTF_8);
                return config;
            }
            String jsonStr = Files.readString(path, StandardCharsets.UTF_8);
            return CodecUtil.decode(CODEC, jsonStr, AppException::new).orThrow();
        } catch (Exception e) {
            log.warn("Failed to load config file, using default config.", e);
            return EMPTY;
        }
    }

}
