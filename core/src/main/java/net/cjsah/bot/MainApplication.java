package net.cjsah.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.config.AppConfig;
import net.cjsah.bot.exception.AppException;

@Slf4j(topic = "Console")
@Getter
public final class MainApplication {
    private static final MainApplication INSTANCE = new MainApplication();
    private WebSocketThread thread = null;

    private final AppConfig config;

    public MainApplication() {
        log.info("初始化文件系统...");
        AppPaths.init();
        log.info("加载配置文件...");
        this.config = AppConfig.loadOrCreate();
    }

    public synchronized void start() {
        if (this.thread != null && this.thread.isAlive()) {
            log.warn("Application has already started; there is no need to start it again.");
            return;
        }
        try {
            this.thread = new WebSocketThread(this.config);
            this.thread.start();
        } catch (Throwable e) {
            throw new AppException("Failed to initialize Websocket Client", e);
        }
    }

    public static MainApplication getInstance() {
        return INSTANCE;
    }


}
