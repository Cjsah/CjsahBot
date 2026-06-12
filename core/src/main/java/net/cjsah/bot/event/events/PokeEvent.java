package net.cjsah.bot.event.events;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public abstract class PokeEvent extends ReceivedEvent {
    private final long userId;
    private final long targetId;

    public static class Builder {
        public static final Codec<PokeEvent> CODEC = Codec.either(
            GroupPokeEvent.CODEC,
            FriendPokeEvent.CODEC
        ).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            event -> event instanceof GroupPokeEvent g
                ? Either.left(g)
                : Either.right((FriendPokeEvent) event)
        );
    }
}
