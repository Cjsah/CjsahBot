package net.cjsah.bot;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginLoader;
import net.cjsah.bot.plugin.PluginThreadPools;
import net.cjsah.bot.util.JsonUtil;
import net.cjsah.bot.util.RequestUtil;
import org.java_websocket.enums.ReadyState;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MainApplication implements Runnable {
    private static final Logger log = LoggerFactory.getLogger("Console");
    private final WebSocketClientImpl wsc;
    private final Scheduler scheduler;
    private final BlockingQueue<SignalType> signals;
    private volatile boolean stop;
    private volatile boolean connecting;

    public MainApplication() throws SchedulerException, URISyntaxException {
        this.wsc = new WebSocketClientImpl("http://127.0.0.1");
        this.scheduler = new StdSchedulerFactory().getScheduler();
        this.stop = false;
        this.connecting = false;
        this.signals = new LinkedBlockingQueue<>();
    }

    private void runApp() throws InterruptedException, SchedulerException {
        log.info("初始化文件系统...");
        FilePaths.init();
        log.info("初始化系统定时器...");
        this.scheduler.start();
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
        log.info("正在取消注册所有事件...");
        EventManager.unsubscribeAll();
        log.info("正在关闭系统定时器...");
        this.scheduler.shutdown(true);
        log.info("已关闭");
    }

    private void tryConnect() throws InterruptedException {
        log.info("正在获取服务器地址...");
        String content = FilePaths.ACCOUNT.read();
        JSONObject json = JsonUtil.deserialize(content);
        String appId = json.getString("appId");
        String secret = json.getString("secret");
        if (Validator.isEmpty(appId)) {
            log.error("appId为空，请先设置appId");
            throw new IllegalArgumentException("appId为空，请先设置appId");
        }
        if (Validator.isEmpty(secret)) {
            log.error("secret为空，请先设置secret");
            throw new IllegalArgumentException("secret为空，请先设置secret");
        }
        String token = "Bot %s.%s".formatted(appId, secret);
        JSONObject body = RequestUtil.request(RequestUtil.get("https://sandbox.api.sgroup.qq.com/gateway").header("Authorization", token));


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
        } catch (InterruptedException | SchedulerException e) {
            Main.sendSignal(SignalType.STOP);
            throw new RuntimeException(e);
        }
    }

}
