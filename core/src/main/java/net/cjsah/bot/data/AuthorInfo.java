package net.cjsah.bot.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record AuthorInfo(String name, Optional<String> url) {
    private static final Codec<AuthorInfo> OBJ_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("name").forGetter(AuthorInfo::name),
        Codec.STRING.optionalFieldOf("url").forGetter(AuthorInfo::url)
    ).apply(instance, AuthorInfo::new));

    public static final Codec<AuthorInfo> CODEC = Codec.either(
        Codec.STRING,
        OBJ_CODEC
    ).xmap(
        either -> either.map(
            str -> new AuthorInfo(str, Optional.empty()),
            obj -> obj
        ),
        author -> author.url().isEmpty()
            ? Either.left(author.name())
            : Either.right(author)
    );
}
