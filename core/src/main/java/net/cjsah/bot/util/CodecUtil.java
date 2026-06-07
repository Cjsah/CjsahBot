package net.cjsah.bot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import java.util.Optional;
import java.util.function.Function;

public final class CodecUtil {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static <T> Either<T, String> decode(Codec<T> codec, String jsonStr) {
        return decode(codec, JsonParser.parseString(jsonStr), Function.identity());
    }

    public static <T> Either<T, String> decode(Codec<T> codec, JsonElement json) {
        return decode(codec, json, Function.identity());
    }

    public static <T, R> Either<T, R> decode(Codec<T> codec, String jsonStr, Function<String, R> failMapping) {
        return decode(codec, JsonParser.parseString(jsonStr), failMapping);
    }

    public static <T, R> Either<T, R> decode(Codec<T> codec, JsonElement json, Function<String, R> failMapping) {
        DataResult<T> parseResult = codec.parse(JsonOps.INSTANCE, json);
        Optional<DataResult.Error<T>> error = parseResult.error();
        Optional<T> result = parseResult.result();
        return result.<Either<T, R>>map(Either::left).orElseGet(() -> {
            String msg = error.map(DataResult.Error::message).orElse("Unknown");
            return Either.right(failMapping.apply(msg));
        });
    }

    public static <T> Either<String, String> encode(Codec<T> codec, T data) {
        return encode(codec, data, Function.identity());
    }

    public static <T, R> Either<String, R> encode(Codec<T> codec, T data, Function<String, R> failMapping) {
        DataResult<JsonElement> parseResult = codec.encodeStart(JsonOps.INSTANCE, data);
        Optional<JsonElement> result = parseResult.result();
        Optional<DataResult.Error<JsonElement>> error = parseResult.error();
        if (result.isPresent()) {
            return Either.left(GSON.toJson(result.get()));
        } else {
            String msg = error.map(DataResult.Error::message).orElse("Unknown");
            return Either.right(failMapping.apply(msg));
        }
    }

    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> clazz) {
        T[] constants = clazz.getEnumConstants();
        return Codec.INT.xmap(it -> constants[it], Enum::ordinal);
    }
}
