package net.cjsah.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.config.AppConfig;
import net.cjsah.bot.exception.AppException;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j(topic = "Console", access = AccessLevel.PUBLIC)
@Getter
public final class MainApplication {
    private static final MainApplication INSTANCE = new MainApplication();
    private final AppConfig config;

    private WebSocketThread thread = null;
    private volatile AppStatus status;
    private final AtomicBoolean StopSig = new AtomicBoolean(false);


    @SneakyThrows
    private MainApplication() {
        this.status = AppStatus.PREPARED;
        log.info("初始化文件系统...");
        AppPaths.init();
        log.info("加载配置文件...");
        this.config = AppConfig.loadOrCreate();
        log.info("初始化权限系统...");
        PermissionManager.getInstance().reload();
        log.info("正在加载插件...");
        PluginManager.init();

        this.status = AppStatus.STARTING;

        log.info("正在连接到服务器...");
        this.start();
        this.status = AppStatus.STARTED;

        while (!StopSig.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        log.info("已触发关闭进程...");
        this.status = AppStatus.STOPPING;
        log.info("正在卸载所有插件...");
        PluginManager.shutdown();
        log.info("等待插件线程关闭...");

        log.info("正在断开连接...");
        this.thread.halt();
        this.status = AppStatus.STOPPED;
    }

    public AppInstantStatus getStatus() {
        return new AppInstantStatus(
            this.status,
            this.thread == null ? WebSocketStatus.DISCONNECTED : this.thread.getStatus()
        );
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

    public void shutdown() {
        this.StopSig.set(true);
    }

    public static MainApplication getInstance() {
        return INSTANCE;
    }


}
