package net.cjsah.bot.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.function.Supplier;

@Slf4j(topic = "Codecs")
public class ExtraCodecs {
    public static final Codec<JsonElement> JSON = Codec.PASSTHROUGH.xmap(
        dynamic -> dynamic.convert(JsonOps.INSTANCE).getValue(),
        object -> new Dynamic<>(JsonOps.INSTANCE, object)
    );

    public static final Codec<JsonObject> JSON_OBJECT = Codec.PASSTHROUGH.comapFlatMap(
        dynamic -> {
            JsonElement value = dynamic.convert(JsonOps.INSTANCE).getValue();
            return value.isJsonObject() ?
                DataResult.success(value.getAsJsonObject()) :
                DataResult.error(() -> "Not a JSON object");
        },
        object -> new Dynamic<>(JsonOps.INSTANCE, object)
    );

    public static final Codec<JsonArray> JSON_ARRAY = Codec.PASSTHROUGH.comapFlatMap(
        dynamic -> {
            JsonElement value = dynamic.convert(JsonOps.INSTANCE).getValue();
            return value.isJsonArray() ?
                DataResult.success(value.getAsJsonArray()) :
                DataResult.error(() -> "Not a JSON array");
        },
        object -> new Dynamic<>(JsonOps.INSTANCE, object)
    );

    public static final Codec<Instant> TIMESTAMP = Codec.LONG.xmap(Instant::ofEpochSecond, Instant::getEpochSecond);

    public static final Codec<Instant> ISO8601 = Codec.STRING.xmap(
        it -> OffsetDateTime.parse(it).toInstant(),
        Instant::toString
    );

    public static final Codec<BufferedImage> IMAGE_PNG = Codec.STRING.flatXmap(
        it -> {
            String base64 = it.replace("data:image/png;base64,", "");
            try (ByteArrayInputStream bis = new ByteArrayInputStream(base64(base64))) {
                return DataResult.success(ImageIO.read(bis));
            } catch (IOException e) {
                return DataResult.error(e::getMessage);
            }
        },
        it -> {
            if (it == null) return DataResult.error(() -> "Image is null");
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(it, "png", baos);
                return DataResult.success("data:image/png;base64," + base64(baos.toByteArray()));
            } catch (IOException e) {
                return DataResult.error(e::getMessage);
            }
        }
    );

    public static <T> Codec<T> supplier(Supplier<T> factory) {
        return Codec.of(Encoder.empty(), Decoder.unit(factory)).codec();
    }

    public static String base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64(String base64) {
        return Base64.getDecoder().decode(base64);
    }

}
