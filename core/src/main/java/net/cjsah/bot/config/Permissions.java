package net.cjsah.bot.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.AppPaths;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.exception.AppException;
import net.cjsah.bot.util.CodecUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j(topic = "PermissionManager")
public record Permissions(int version, PermissionGlobal global, Map<String, PermissionPlugin> plugins) {
    public static Codec<Permissions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.optionalFieldOf("version", 1_00_00).forGetter(Permissions::version),
        PermissionGlobal.CODEC.fieldOf("global").forGetter(Permissions::global),
        PermissionPlugin.PLUGINS_CODEC.fieldOf("plugins").forGetter(Permissions::plugins)
    ).apply(instance, Permissions::new));

    public static final Permissions EMPTY = new Permissions(1_00_00, PermissionGlobal.EMPTY, Map.of());

    public Permissions replacePlugin(String pluginId, PermissionPlugin plugin) {
        ImmutableMap.Builder<String, PermissionPlugin> builder = ImmutableMap.builder();
        builder.putAll(this.plugins);
        builder.put(pluginId, plugin);

        return new Permissions(this.version(), this.global(), builder.buildKeepingLast());
    }

    public Permissions removePlugin(String pluginId) {
        ImmutableMap.Builder<String, PermissionPlugin> builder = ImmutableMap.builder();
        for (Map.Entry<String, PermissionPlugin> entry : this.plugins.entrySet()) {
            if (entry.getKey().equals(pluginId)) continue;
            builder.put(entry.getKey(), entry.getValue());
        }

        return new Permissions(this.version(), this.global(), builder.buildKeepingLast());
    }

    public static Permissions loadOrCreate() {
        Path path = AppPaths.PERMISSIONS;
        try {
            if (!Files.isRegularFile(path)) {
                Files.createDirectories(path.getParent());
                Permissions config = EMPTY;
                String json = CodecUtil.encode(CODEC, config, AppException::new).orThrow();
                Files.writeString(path, json, StandardCharsets.UTF_8);
                return config;
            }
            String jsonStr = Files.readString(path, StandardCharsets.UTF_8);
            return CodecUtil.decode(CODEC, jsonStr, AppException::new).orThrow();
        } catch (IOException e) {
            log.warn("Failed to load config file, using default config.", e);
            return EMPTY;
        }
    }

    public static boolean save(Permissions permissions) {
        Path path = AppPaths.PERMISSIONS;
        try {
            Files.createDirectories(path.getParent());
            String json = CodecUtil.encode(CODEC, permissions, AppException::new).orThrow();
            Files.writeString(path, json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.warn("Failed to save config file.", e);
            return false;
        }
    }

}
