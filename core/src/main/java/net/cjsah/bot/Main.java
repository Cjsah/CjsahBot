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
    private static final BlockingQueue<SignalType> SignalQueue = new LinkedBlockingQueue<>();
    private static volatile MainThread CurrentMainThread;

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!CurrentMainThread.stop) Main.sendSignal(SignalType.STOP);
            try {
                CurrentMainThread.join();
            } catch (InterruptedException e) {
                log.error("Error while shutting down", e);
            }
        }));

        CurrentMainThread = new MainThread();
        CurrentMainThread.start();

        running:
        while (true) {
            switch (SignalQueue.take()) {
                case STOP -> {
                    Main.sendAppSignal(SignalType.STOP);
                    break running;
                }
                case RE_CONNECT -> Main.sendAppSignal(SignalType.RE_CONNECT);
                case RESTART -> {
                    Main.sendAppSignal(SignalType.STOP);
                    CurrentMainThread.join();
                    CurrentMainThread = new MainThread();
                    CurrentMainThread.start();
                }
            }
        }
    }

    private static void sendAppSignal(SignalType signal) throws InterruptedException {
        while (!CurrentMainThread.signals.offer(signal)) {
            TimeUnit.MICROSECONDS.sleep(100);
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

    public static boolean isConnecting() {
        return CurrentMainThread.connecting;
    }

    public static boolean isRunning() {
        return !CurrentMainThread.stop;
    }

    private static class MainThread extends Thread {
        private static final Logger log = LoggerFactory.getLogger("Console");
        private final WebSocketClientImpl wsc;
        private final BlockingQueue<SignalType> signals;
        private volatile boolean stop;
        private volatile boolean connecting;

        public MainThread() throws Exception {
            try {
                String content = FilePaths.ACCOUNT.read();
                JSONObject json = JsonUtil.deserialize(content);
                String token = json.getString("token");
                if (token == null || token.isEmpty()) {
                    log.error("token为空，请先设置token");
                    throw new IllegalArgumentException("token为空，请先设置token");
                }
                Api.setToken(token);
                this.wsc = new WebSocketClientImpl(token);
            } catch (Throwable e) {
                log.error("初始化失败!", e);
                throw e;
            }
            this.stop = false;
            this.connecting = false;
            this.signals = new LinkedBlockingQueue<>();
        }

        private void runApp() throws InterruptedException {
            log.info("初始化文件系统...");
            FilePaths.init();
            log.info("初始化权限系统...");
            PermissionManager.init();
            log.info("初始化插件系统...");
            PluginThreadPools.init();
            log.info("正在加载插件...");
            PluginLoader.loadPlugins();

            this.tryConnect();

            PluginLoader.onStarted();

            running:
            while (true) {
                switch (signals.take()) {
                    case STOP -> {
                        break running;
                    }
                    case RE_CONNECT -> this.tryConnect();
                }
            }

            log.info("执行关闭命令...");
            this.stop = true;
            log.info("正在卸载所有插件...");
            PluginLoader.unloadPlugins();
            log.info("等待插件线程关闭...");
            PluginThreadPools.awaitShutdown();
            log.info("正在断开连接...");
            this.wsc.shutdown();
            log.info("已关闭");
        }

        private void tryConnect() throws InterruptedException {
            log.info("正在连接到服务器...");
            this.connecting = true;
            while (!this.stop) {
                if (this.wsc.getReadyState() == ReadyState.NOT_YET_CONNECTED ?
                        this.wsc.connectBlocking() :
                        this.wsc.reconnectBlocking()
                ) {
                    break;
                }
                log.warn("连接失败, 将在 3 秒后重试...");
                TimeUnit.SECONDS.sleep(3);
            }
            this.connecting = false;
            if (this.stop) {
                log.info("程序关闭中, 停止连接");
            }
        }

        @Override
        public void run() {
            try {
                this.runApp();
            } catch (InterruptedException e) {
                Main.sendSignal(SignalType.STOP);
                throw new RuntimeException(e);
            }
        }
    }

}
