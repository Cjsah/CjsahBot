package net.cjsah.bot.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.AuthorInfo;
import net.cjsah.bot.util.ExtraCodecs;

import java.util.List;

public record PluginMetadata(String id, String version, String name, String desc, List<AuthorInfo> authors, String entrypoint, JsonElement extra) {
    public static final Codec<PluginMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(PluginMetadata::id),
        Codec.STRING.fieldOf("version").forGetter(PluginMetadata::version),
        Codec.STRING.fieldOf("name").forGetter(PluginMetadata::name),
        Codec.STRING.optionalFieldOf("desc", "").forGetter(PluginMetadata::desc),
        AuthorInfo.CODEC.listOf().optionalFieldOf("authors", List.of()).forGetter(PluginMetadata::authors),
        Codec.STRING.fieldOf("entrypoint").forGetter(PluginMetadata::entrypoint),
        ExtraCodecs.JSON.optionalFieldOf("extra", JsonNull.INSTANCE).forGetter(PluginMetadata::extra)
    ).apply(instance, PluginMetadata::new));
}
