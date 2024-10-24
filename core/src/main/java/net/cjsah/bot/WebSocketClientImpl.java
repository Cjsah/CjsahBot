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

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientImpl extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger("WebsocketClient");
    private final HeartBeatTimer heart;

    public WebSocketClientImpl(String token) throws URISyntaxException, SchedulerException {
        super(new URI("wss://chat.xiaoheihe.cn/chatroom/ws/connect?chat_os_type=bot"));
        this.addHeader("client_type", "heybox_chat");
        this.addHeader("x_client_type", "web");
        this.addHeader("os_type", "web");
        this.addHeader("x_os_type", "bot");
        this.addHeader("x_app", "heybox_chat");
        this.addHeader("chat_version", "1.24.5");
        this.addHeader("token", token);
        this.heart = new HeartBeatTimer(() -> {
            if (this.isOpen()) {
                this.send("PING");
            }
        }, () -> Main.sendSignal(SignalType.RE_CONNECT));
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

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("连接成功!");
        try {
            this.heart.start();
        } catch (SchedulerException e) {
            log.error("无法启动心跳服务!", e);
            Main.sendSignal(SignalType.RE_CONNECT);
        }
    }

    @Override
    public void onMessage(String msg) {
        log.debug("收到消息: {}", msg);
        if ("PONG".equals(msg)) {
            this.heart.heartPong();
            return;
        }
        JSONObject json = JsonUtil.deserialize(msg);
        EventManager.parseEvent(json);
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
        if (Main.isRunning()) {
            Main.sendSignal(SignalType.RE_CONNECT);
        }
    }
}
