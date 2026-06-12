package net.cjsah.bot;

import lombok.Getter;
import net.cjsah.bot.config.AppConfig;

public class WebSocketThread extends Thread {
    private final AppConfig config;
    private final WebSocketClientImpl webSocketClient;
    @Getter
    private WebSocketStatus status;

    public WebSocketThread(AppConfig config) throws Exception {
        this.config = config;
        this.webSocketClient = new WebSocketClientImpl(this, config.url(), config.token());
        this.status = WebSocketStatus.DISCONNECTED;
    }

    @Override
    public void run() {
        this.status = WebSocketStatus.CONNECTING;
    }
}
