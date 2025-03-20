package net.cjsah.bot;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.ConnectionHelloEvent;
import net.cjsah.bot.event.events.ConnectionReadyEvent;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.type.Opcode;
import net.cjsah.bot.plugin.MainPlugin;
import net.cjsah.bot.util.JsonUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public final class WebSocketClientImpl extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger("WebsocketClient");
    private final HeartBeatTimer heart = new HeartBeatTimer(this::sendMsg);
    private String token;

    public WebSocketClientImpl(String url) throws URISyntaxException, SchedulerException {
        super(new URI(url));
        String pluginId = MainPlugin.PLUGIN_INFO.getId();
        EventManager.subscribe(pluginId, ConnectionHelloEvent.class, event -> {
            log.info("正在进行鉴权认证...");
            this.heart.setHeartTime(event.getHeartbeatInterval());
            JSONObject payload = Opcode.IDENTIFY.generate(false, json -> {
                json.put("token", token);
                json.put("intents",
//                        1 << 0 |
                        1 << 1 |
//                        1 << 9 |
                        1 << 10 |
                        1 << 12 |
//                        1 << 18 |
//                        1 << 19 |
                        1 << 25 |
                        1 << 26 |
//                        1 << 27 |
//                        1 << 28 |
//                        1 << 29 |
                        1 << 30
                );
            });
            this.send(payload.toString());
        });
        EventManager.subscribe(pluginId, ConnectionReadyEvent.class, event -> { // TODO 如果是resume, 需要记录session
            try {
                log.info("连接成功, 正在启动心跳服务...");
                this.heart.start();
                log.info("启动成功!");
            } catch (SchedulerException e) {
                log.error("无法启动心跳服务!", e);
                this.heart.stop();
                Main.sendSignal(SignalType.RE_CONNECT);
            }
        });
        EventManager.subscribe(pluginId, HeartbeatEvent.class, event -> this.heart.hearted());
    }

    private void sendMsg(JSONObject payload) {
        String value = payload.toString();
        log.debug("发送消息: {}", value);
        this.send(value);
    }

    @Override
    public void close() {
        this.heart.stop();
        super.close();
    }

    public void shutdown() throws InterruptedException {
        this.heart.cancel();
        this.closeBlocking();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("成功建立连接, 等待下一步操作...");
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
            Main.sendSignal(SignalType.STOP);
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
