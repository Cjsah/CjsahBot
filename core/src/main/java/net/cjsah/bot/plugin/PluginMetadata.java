package net.cjsah.bot.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import net.cjsah.bot.data.AuthorInfo;
import net.cjsah.bot.util.CodecUtil;

import java.util.List;

@Data
public class PluginMetadata {
    public static final Codec<PluginMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(PluginMetadata::getId),
        Codec.STRING.fieldOf("version").forGetter(PluginMetadata::getVersion),
        Codec.STRING.fieldOf("name").forGetter(PluginMetadata::getName),
        Codec.STRING.optionalFieldOf("desc", "").forGetter(PluginMetadata::getDesc),
        AuthorInfo.CODEC.listOf().optionalFieldOf("authors", List.of()).forGetter(PluginMetadata::getAuthors),
        Codec.STRING.fieldOf("entrypoint").forGetter(PluginMetadata::getEntrypoint),
        CodecUtil.JSON.optionalFieldOf("extra", JsonNull.INSTANCE).forGetter(PluginMetadata::getExtra)
    ).apply(instance, PluginMetadata::new));

    private final String id;
    private final String version;
    private final String name;
    private final String desc;
    private final List<AuthorInfo> authors;
    private final String entrypoint;
    private final JsonElement extra;

}
