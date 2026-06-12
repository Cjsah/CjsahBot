package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FileInfo(String id, String name, long size, long busid) {
    public static final Codec<FileInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(FileInfo::id),
        Codec.STRING.fieldOf("name").forGetter(FileInfo::name),
        Codec.LONG.fieldOf("size").forGetter(FileInfo::size),
        Codec.LONG.fieldOf("busid").forGetter(FileInfo::busid)
    ).apply(instance, FileInfo::new));
}
