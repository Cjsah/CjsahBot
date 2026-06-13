package net.cjsah.bot.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.cjsah.bot.data.AuthorInfo;
import net.cjsah.bot.loader.PluginClassLoader;
import net.cjsah.bot.util.CodecUtil;

import java.util.List;

@Data
public class PluginMetadata {
    private final PluginClassLoader loader;
    private final String id;
    private final String version;
    private final String name;
    private final String desc;
    private final List<AuthorInfo> authors;
    private final PluginEntrypoint entrypoint;
    private final JsonElement extra;


    @Getter
    @Setter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public static class Builder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(Builder::id),
            Codec.STRING.fieldOf("version").forGetter(Builder::version),
            Codec.STRING.fieldOf("name").forGetter(Builder::name),
            Codec.STRING.optionalFieldOf("desc", "").forGetter(Builder::desc),
            AuthorInfo.CODEC.listOf().optionalFieldOf("authors", List.of()).forGetter(Builder::authors),
            Codec.STRING.fieldOf("entrypoint").forGetter(Builder::entrypoint),
            CodecUtil.JSON.optionalFieldOf("extra", JsonNull.INSTANCE).forGetter(Builder::extra)
        ).apply(instance, Builder::new));

        private final String id;
        private final String version;
        private final String name;
        private final String desc;
        private final List<AuthorInfo> authors;
        private final String entrypoint;
        private final JsonElement extra;

        public PluginMetadata build(PluginClassLoader loader) {
            return new PluginMetadata(
                loader,
                this.id, this.version, this.name, this.desc, this.authors,
                new PluginEntrypoint(this.entrypoint), this.extra
            );
        }
    }
}
