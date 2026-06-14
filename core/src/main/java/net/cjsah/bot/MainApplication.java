package net.cjsah.bot;

import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.config.AppConfig;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.util.LateInit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j(topic = "Console", access = AccessLevel.PUBLIC)
public final class MainApplication {
    private static final LateInit<MainApplication> INSTANCE = LateInit.of();

    private final LateInit<WebSocketClientImpl> webSocketClient = LateInit.of();
    private final AppConfig config;
    private final CountDownLatch stopLatch;

    private volatile AppStatus status;

    static void main(String[] args) throws InterruptedException {
        log.info("正在初始化文件系统...");
        AppPaths.init();

        MainApplication app = new MainApplication();
        INSTANCE.set(app);

        log.info("正在初始化权限系统...");
        PermissionManager.getInstance().reload();
        log.info("正在加载插件...");
        PluginManager.registry();

        app.status = AppStatus.RUNNING;

        app.onStart();

        app.stopLatch.await();

        log.info("正在执行关闭流程...");
        app.onStop();
    }

    private MainApplication() {
        this.stopLatch = new CountDownLatch(1);
        this.status = AppStatus.INIT;
        log.info("正在加载配置文件...");
        this.config = AppConfig.loadOrCreate();
    }

    public AppInstantStatus getStatus() {
        return new AppInstantStatus(
            this.status,
            this.webSocketClient.getOptional()
                .map(WebSocketClientImpl::getStatus)
                .orElse(WebSocketStatus.DISCONNECTED)
        );
    }

    public synchronized void onStart() {
        log.info("正在连接到服务器...");
        AtomicReference<WebSocketClientImpl> reference = new AtomicReference<>();
        Thread thread = new Thread(() -> reference.get().start(), "Websocket thread");
        thread.setUncaughtExceptionHandler((_, throwable) -> log.error("Uncaught exception in server thread", throwable));
        if (Runtime.getRuntime().availableProcessors() > 4) {
            thread.setPriority(8);
        }

        WebSocketClientImpl ws = new WebSocketClientImpl(thread, this.config.toURI());
        reference.set(ws);
        thread.start();
        this.webSocketClient.set(ws);
    }

    private void onStop() throws InterruptedException {
        this.status = AppStatus.STOPPING;
        log.info("正在卸载所有插件...");
        PluginManager.halt(true);
        log.info("正在断开连接...");
        this.webSocketClient.get().halt(true);
        this.status = AppStatus.STOPPED;
    }

    public void halt() {
        this.stopLatch.countDown();
    }

    public static MainApplication getInstance() {
        return INSTANCE.orElseThrow(BuiltinExceptions.APP_NOT_INIT::create);
    }
}
