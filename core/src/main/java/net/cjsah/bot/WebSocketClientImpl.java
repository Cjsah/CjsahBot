package net.cjsah.bot;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.util.JsonUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public final class WebSocketClientImpl extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger("WebsocketClient");
    private final HeartBeatTimer heart = new HeartBeatTimer();

    public WebSocketClientImpl(String url, String token) throws URISyntaxException, SchedulerException {
        super(new URI(url + "?access_token=" + token));
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
            JSONObject json = JsonUtil.deserialize(msg);
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
