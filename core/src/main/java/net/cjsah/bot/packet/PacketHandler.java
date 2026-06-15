package net.cjsah.bot.packet;

import com.mojang.datafixers.util.Either;
import net.cjsah.bot.packet.request.payload.RequestPacket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketHandler {
    private static final PacketHandler INSTANCE = new PacketHandler();

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public <T> Either<T, String> send(RequestPacket packet) {

        return null;
    }







    public static PacketHandler getInstance() {
        return INSTANCE;
    }

}
