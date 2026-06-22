package net.cjsah.bot.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public class RequestUtil {

    public static <T> Either<T, Error> get(String url, Codec<T> codec) {
        return request(HttpRequest.get(url), codec);
    }

    public static <T> Either<T, Error> post(String url, String body, Codec<T> codec) {
        HttpRequest request = HttpRequest.post(url)
            .header("Content-Type", "application/json")
            .body(body);
        return request(request, codec);
    }

    public static <T> Either<T, Error> request(HttpRequest request, Codec<T> codec) {
        try (HttpResponse response = request.execute()) {
            if (response.getStatus() != 200) {
                return Either.right(new Error(response.getStatus(), response.body()));
            }
            String body = response.body();
            Either<T, String> decode = CodecUtil.decode(codec, body);
            if (decode.isLeft()) {
                T value = decode.left().orElseThrow();
                return Either.left(value);
            }
            String error = decode.right().orElseThrow();
            return Either.right(new Error(1500, error));
        }
    }

    public record Error(int code, String message) {
    }
}
