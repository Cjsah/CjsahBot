package net.cjsah.bot.packet;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import lombok.Setter;
import net.cjsah.bot.packet.request.RequestPayload;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class PacketHandler {
    private static final Logger log = LoggerFactory.getLogger("PacketHandler");
    private static final PacketHandler INSTANCE = new PacketHandler();
    private static final long TIMEOUT_SECONDS = 60;

    private final Map<String, Pending<?>> pending;
    @Setter
    private Consumer<String> sender = null;

    public PacketHandler() {
        this.pending = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> Either<T, String> send(RequestPacket request) {
        if (this.sender == null) {
            return Either.right("WebSocket sender not configured");
        }

        RequestPayload<?> payload = request.packet();
        String echo = payload.getEcho();

        Either<String, String> either = RequestType.encode(payload);
        Optional<String> error = either.right();
        if (error.isPresent()) {
            return Either.right(error.get());
        }

        CompletableFuture<Either<T, String>> future = new CompletableFuture<>();
        Pending<T> p = new Pending<>(future, (Codec<T>) request.getResponse());
        this.pending.put(echo, p);

        log.debug("Sending packet echo={} action={}", echo, payload.getAction());
        this.sender.accept(either.left().get());

        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            this.pending.remove(echo);
            log.warn("Packet echo={} timed out after {}s", echo, TIMEOUT_SECONDS);
            return Either.right("Request timed out");
        } catch (InterruptedException e) {
            this.pending.remove(echo);
            return Either.right("Request interrupted");
        } catch (ExecutionException e) {
            this.pending.remove(echo);
            log.error("Packet echo={} failed", echo, e);
            return Either.right("Request failed: " + e.getCause().getMessage());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void receive(ResponseBuilder response) {
        Pending p = this.pending.remove(response.getEcho());
        if (p == null) {
            log.debug("No pending packet for echo={}", response.getEcho());
            return;
        }
        Either data = response.build(p.codec);
        log.debug("Received response for echo={}: [{}]{}", response.getEcho(), response.getStatus(), data);
        p.future.complete(data);
    }

    public void clean() {
        this.sender = null;
        for (Pending<?> p : this.pending.values()) {
            p.future.complete(Either.right("Connection closed"));
        }
        this.pending.clear();
        log.info("Cleared all pending packets");
    }

    private record Pending<T>(CompletableFuture<Either<T, String>> future, Codec<T> codec) {
    }

    public static PacketHandler getInstance() {
        return INSTANCE;
    }
}
