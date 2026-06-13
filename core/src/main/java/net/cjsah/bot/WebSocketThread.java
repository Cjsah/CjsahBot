package net.cjsah.bot;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.config.AppConfig;
import org.java_websocket.enums.ReadyState;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j(topic = "WebSocket")
public class WebSocketThread extends Thread {
    private final AppConfig config;
    private final WebSocketClientImpl webSocketClient;
    @Getter
    private WebSocketStatus status;
    private final AtomicBoolean stopSig = new AtomicBoolean(false);

    public WebSocketThread(AppConfig config) throws Exception {
        this.config = config;
        this.webSocketClient = new WebSocketClientImpl(this, config.url(), config.token());
        this.status = WebSocketStatus.DISCONNECTED;
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("正在连接到服务器...");
        while (!this.stopSig.get()) {
            this.status = WebSocketStatus.CONNECTING;
            if (this.webSocketClient.getReadyState() == ReadyState.NOT_YET_CONNECTED ?
                this.webSocketClient.connectBlocking() :
                this.webSocketClient.reconnectBlocking()
            ) {
                this.status = WebSocketStatus.CONNECTED;
                break;
            }
            this.status = WebSocketStatus.DISCONNECTED;
            log.warn("连接失败, 将在 3 秒后重试...");
            TimeUnit.SECONDS.sleep(3);
        }
        if (this.stopSig.get()) {
            log.info("程序关闭中, 停止连接");
        }
    }

    @SneakyThrows
    public void halt() {
        this.join();
    }

}
