package net.cjsah.bot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.data.Countdown;
import net.cjsah.bot.event.EventManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j(topic = "WebSocket")
public final class WebSocketClientImpl extends WebSocketClient {
    private static final AtomicLong ID_CACHE = new AtomicLong(0L);
    private final Thread workerThread;
    @Getter
    private final long id;
    private final Countdown connectCountdown = new Countdown(60);
    @Getter
    private WebSocketStatus status;
    private volatile boolean running = true;

    public WebSocketClientImpl(Thread thread, URI uri) {
        super(uri);
        this.workerThread = thread;
        this.id = ID_CACHE.incrementAndGet();
        this.status = WebSocketStatus.DISCONNECTED;
    }

    @SneakyThrows
    public void start() {
        while (this.running) {
            if (this.connectCountdown.tick()) {
                this.status = WebSocketStatus.CONNECTING;

                if (this.getReadyState() == ReadyState.NOT_YET_CONNECTED) {
                    this.connect();
                } else {
                    this.reconnect();
                }
            }
            TimeUnit.MILLISECONDS.sleep(50);
        }
        this.closeBlocking();
    }

    public void halt(boolean await) {
        this.running = false;
        if (await) {
            try {
                this.workerThread.join();
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        HeartBeatTimer.getInstance().register(this);
        this.status = WebSocketStatus.CONNECTED;
        log.info("连接成功!");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        HeartBeatTimer.getInstance().deregister(this.id);
        this.status = WebSocketStatus.DISCONNECTED;
        if (code == CloseFrame.NORMAL) return;
        this.connectCountdown.reset();
        log.warn("连接断开: [{}]{}, 将在 3 秒后重试...", code, reason);
    }

    @Override
    public void onMessage(String msg) {
        log.debug("收到消息: {}", msg);
        try {
            JsonElement json = JsonParser.parseString(msg);
            EventManager.parseWebSocketEvent(this.id, json);
        } catch (Throwable e) {
            log.error("出现错误!", e);
        }
    }

    @Override
    public void onError(Exception e) {
        log.error("出现错误", e);
    }
}
