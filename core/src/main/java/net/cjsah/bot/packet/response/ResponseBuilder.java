package net.cjsah.bot.packet.response;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.CodecUtil;

@Getter
@RequiredArgsConstructor
public class ResponseBuilder {
    public static final Codec<ResponseBuilder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResponseStatus.CODEC.fieldOf("status").forGetter(ResponseBuilder::getStatus),
        Codec.INT.fieldOf("retcode").forGetter(ResponseBuilder::getRetcode),
        CodecUtil.JSON.fieldOf("data").forGetter(ResponseBuilder::getData),
        Codec.STRING.fieldOf("echo").forGetter(ResponseBuilder::getEcho)
    ).apply(instance, ResponseBuilder::new));

    private final ResponseStatus status;
    private final int retcode;
    private final JsonElement data;
    private final String echo;

    public <T> Either<T, String> build(Codec<T> codec) {
        if (this.status != ResponseStatus.OK || this.retcode != 0) {
            return Either.right("status: %s, code: %s".formatted(this.status, this.retcode));
        }
        return CodecUtil.decode(codec, this.data);
    }

}
