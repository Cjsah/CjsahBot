package net.cjsah.bot;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.Api;
import net.cjsah.bot.event.CancelableEvent;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginLoader;
import net.cjsah.bot.plugin.PluginThreadPools;
import net.cjsah.bot.util.JsonUtil;
import org.java_websocket.enums.ReadyState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger log = LoggerFactory.getLogger("Console");
    private static final WebSocketClientImpl WebSocketClient;
    private static final BlockingQueue<SignalType> SignalQueue = new LinkedBlockingQueue<>();
    private static boolean Stop = false;
    private static Thread mainThread;

    public static void main(String[] args) throws InterruptedException {
        mainThread = Thread.currentThread();
        log.info("初始化文件系统...");
        FilePaths.init();
        log.info("初始化权限系统...");
        PermissionManager.init();
        log.info("正在加载插件...");
        PluginLoader.loadPlugins();

        Main.tryConnect();

        PluginLoader.onStarted();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.sendSignal(SignalType.STOP);
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                log.error("Error while shutting down", e);
            }
        }));

        running:
        while (true) {
            switch (SignalQueue.take()) {
                case STOP -> {
                    break running;
                }
                case RESTART -> log.warn("未完成, 请手动重启!");
                case RE_CONNECT -> Main.tryConnect();
            }
        }

        log.info("执行关闭命令...");
        Main.Stop = true;
        log.info("正在卸载所有插件...");
        PluginLoader.unloadPlugins();
        log.info("等待插件线程关闭...");
        PluginThreadPools.awaitShutdown();
        log.info("正在断开连接...");
        WebSocketClient.shutdown();
        log.info("已关闭");
    }

    static {
        try {
            String content = FilePaths.ACCOUNT.read();
            JSONObject json = JsonUtil.deserialize(content);
            String token = json.getString("token");
            if (token == null || token.isEmpty()) {
                log.error("token为空，请先设置token");
                throw new IllegalArgumentException("token为空，请先设置token");
            }
            Api.setToken(token);
            WebSocketClient = new WebSocketClientImpl(token);
        } catch (Throwable e) {
            log.error("初始化失败!", e);
            throw new RuntimeException(e);
        }
    }

    public static void tryConnect() throws InterruptedException {
        log.info("正在连接到服务器...");
        while (Main.isRunning()) {
            if (WebSocketClient.getReadyState() == ReadyState.NOT_YET_CONNECTED ?
                    WebSocketClient.connectBlocking() :
                    WebSocketClient.reconnectBlocking()
            ) {
                break;
            }
            log.warn("连接失败, 将在 3 秒后重试...");
            TimeUnit.SECONDS.sleep(3);
        }
        if (!Main.isRunning()) {
            log.info("程序关闭中, 停止连接");
        }
    }

    public static void sendSignal(SignalType signal) {
        log.info("触发信号: {}", signal);
        CancelableEvent event = signal.getEvent().get();
        if (event != null) {
            EventManager.broadcast(event);
            if (event.isCancel()) {
                log.info("取消触发: {}", signal);
                return;
            }
        }
        if (!SignalQueue.offer(signal)) {
            log.warn("触发 {} 失败, 请重试!", signal);
        }
    }

    public static boolean isRunning() {
        return !Stop;
    }

}
