package net.cjsah.bot;

import net.cjsah.bot.config.AppConfig;

public class WebSocketThread extends Thread {
    private final AppConfig config;
    private final WebSocketClientImpl webSocketClient;

    public WebSocketThread(AppConfig config) throws Exception {
        this.config = config;
        this.webSocketClient = new WebSocketClientImpl(this, config.url(), config.token());
    }

    @Override
    public void run() {

    }
}
