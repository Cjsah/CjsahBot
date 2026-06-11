package net.cjsah.bot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.event.EventManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import org.quartz.SchedulerException;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j(topic = "WebsocketClient")
public final class WebSocketClientImpl extends WebSocketClient {
    private final WebSocketThread thread;
    private final HeartBeatTimer heart = new HeartBeatTimer();

    public WebSocketClientImpl(WebSocketThread thread, String url, String token) throws URISyntaxException, SchedulerException {
        super(new URI(url + "?access_token=" + token));
        this.thread = thread;
    }

    @Override
    public void close() {
        super.close();
        this.heart.stop();
    }

    public void shutdown() throws InterruptedException {
        this.closeBlocking();
        this.heart.cancel();
    }

    public void lifecycle(boolean heart, long addition) {
        if (heart) this.heart.heart(addition);
        else this.heart.lifecycle();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("连接成功!");
        try {
            this.heart.start();
        } catch (SchedulerException e) {
            log.error("无法启动心跳服务!", e);
            this.heart.stop();
            Main.sendSignal(SignalType.RE_CONNECT);
        }
    }

    @Override
    public void onMessage(String msg) {
        log.debug("收到消息: {}", msg);
        try {
            JsonElement json = JsonParser.parseString(msg);
            EventManager.parseEvent(json);
        } catch (Throwable e) {
            log.error("出现错误!", e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (code == CloseFrame.NORMAL) return;
        log.warn("连接断开: [{}]{}", code, reason);
        if (Main.isRunning()) {
            Main.sendSignal(SignalType.RE_CONNECT);
        }
    }

    @Override
    public void onError(Exception e) {
        log.error("出现错误", e);
//        if (Main.isRunning()) {
//            Main.sendSignal(SignalType.RE_CONNECT);
//        }
    }
}
