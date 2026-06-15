package net.cjsah.bot.packet.request;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import net.cjsah.bot.MainApplication;
import net.cjsah.bot.packet.request.payload.RequestPacket;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class RequestPayload<T extends RequestPacket> {
    private static final AtomicLong ID_CACHE = new AtomicLong(0);
    private final RequestType action;
    private final String echo;
    private final T payload;

    public RequestPayload(RequestType action, T payload) {
        this.echo = "p%s-t%s".formatted(MainApplication.PID, ID_CACHE.incrementAndGet());
        this.action = action;
        this.payload = payload;
    }

    public static <N extends RequestPacket> Codec<RequestPayload<N>> codec(Codec<N> codec) {
        return RecordCodecBuilder.create(instance -> instance.group(
            RequestType.CODEC.fieldOf("action").forGetter(RequestPayload::getAction),
            Codec.STRING.fieldOf("echo").forGetter(RequestPayload::getEcho),
            codec.fieldOf("params").forGetter(RequestPayload::getPayload)
        ).apply(instance, (action, _, payload) -> new RequestPayload<>(action, payload)));
    }
}
