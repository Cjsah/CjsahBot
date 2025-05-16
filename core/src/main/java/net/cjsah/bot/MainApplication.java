package net.cjsah.bot;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.Api;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CancelableEvent;
import net.cjsah.bot.http.BotHttpServerImpl;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginLoader;
import net.cjsah.bot.plugin.PluginThreadPools;
import net.cjsah.bot.util.DateUtil;
import net.cjsah.bot.util.JsonUtil;
import net.cjsah.bot.util.RequestUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainApplication extends Thread {
    private static final Logger log = LoggerFactory.getLogger("Console");
    private static volatile MainApplication INSTANCE = null;
    private static volatile boolean RESTART = false;
    private final BotHttpServerImpl httpServer;
    private final Scheduler scheduler;
    private final BlockingQueue<SignalType> signals;
    private volatile boolean stop;

    public MainApplication() throws SchedulerException, IOException {
        this.scheduler = new StdSchedulerFactory().getScheduler();
        this.httpServer = new BotHttpServerImpl();
        this.stop = false;
        this.signals = new LinkedBlockingQueue<>();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!INSTANCE.stop) MainApplication.sendSignal(SignalType.STOP);
            try {
                INSTANCE.join();
            } catch (InterruptedException e) {
                log.error("Error while shutting down", e);
            }
        }));

        do {
            try {
                RESTART = false;
                INSTANCE = new MainApplication();
                INSTANCE.start();
                INSTANCE.join();
            } catch (Exception e) {
                log.error("Application Error!", e);
                break;
            }
        } while (!INSTANCE.stop || RESTART);
    }

    public static void sendSignal(SignalType signal) {
        log.info("触发信号: {}", signal);
        CancelableEvent event = signal.getEvent().get();
        if (INSTANCE.signals.contains(SignalType.STOP) || INSTANCE.signals.contains(signal)) {
            log.info("取消触发: {}", signal);
            return;
        }
        if (event != null) {
            EventManager.broadcast(event);
            if (event.isCancel()) {
                log.info("取消触发: {}", signal);
                return;
            }
        }
        if (signal == SignalType.STOP) INSTANCE.signals.clear();
        if (!INSTANCE.signals.offer(signal)) {
            log.warn("触发 {} 失败, 请重试!", signal);
        }
    }

    public static boolean isRunning() {
        return !INSTANCE.stop;
    }

    @Override
    public void run() {
        try {
            this.runApp();
        } catch (Exception e) {
            MainApplication.sendSignal(SignalType.STOP);
            throw new RuntimeException(e);
        } finally {
            this.unload();
            log.info("已关闭");
        }
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

        String content = FilePaths.ACCOUNT.read();
        JSONObject config = JsonUtil.deserialize(content);
        String appId = config.getString("appId");
        String secret = config.getString("secret");
        int port = config.getIntValue("port", 8080);
        if (Validator.isEmpty(appId)) {
            throw new IllegalArgumentException("appId为空，请先设置appId");
        }
        if (Validator.isEmpty(secret)) {
            throw new IllegalArgumentException("secret为空，请先设置secret");
        }

        this.apiConnect(appId, secret);
        this.listenHttpHooks(appId, secret, port);

        PluginLoader.onStarted();

        running:
        while (!this.stop) {
            switch (signals.take()) {
                case RESTART:
                    RESTART = true;
                case STOP:
                    break running;
                default:
                    break;
            }
        }
    }

    private void unload() {
        try {
            log.info("执行关闭命令...");
            this.stop = true;
            log.info("正在卸载所有插件...");
            PluginLoader.unloadPlugins();
            log.info("等待插件线程关闭...");
            PluginThreadPools.awaitShutdown();
            log.info("正在断开连接...");
            this.httpServer.shutdown();
            log.info("正在取消注册所有事件...");
            EventManager.unsubscribeAll();
            log.info("正在关闭系统定时器...");
            this.scheduler.shutdown(true);
        }catch (InterruptedException | SchedulerException e) {
            log.error("Stop Bot Failed!", e);
            throw new RuntimeException(e);
        }
    }

    private void listenHttpHooks(String appId, String secret, int port) {
        try {
            log.info("正在启动Http服务器...");
            this.httpServer.init(port, appId, secret);
            this.httpServer.start();
        } catch (Exception e) {
            log.error("Http Hooks create failed!", e);
            throw new RuntimeException(e);
        }
    }

    private void apiConnect(String appId, String secret) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey("Trigger", "API_TOKEN");
        JobKey jobKey = new JobKey("Job", "API_TOKEN");
        if (this.scheduler.checkExists(triggerKey)) return;
        JobDataMap map = new JobDataMap();
        map.put("appId", appId);
        map.put("secret", secret);
        map.put("expires", 0L);
        JobDetail job = JobBuilder
                .newJob(ApiTokenRefresherJob.class)
                .withIdentity(jobKey)
                .usingJobData(map)
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
                .build();
        this.scheduler.scheduleJob(job, trigger);
    }

    @PersistJobDataAfterExecution
    @DisallowConcurrentExecution
    public static class ApiTokenRefresherJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            long expires = map.getLong("expires");
            if (expires > DateUtil.nowTimeStamp() + 30) return;
            log.info("正在获取API Token...");
            String appId = map.getString("appId");
            String secret = map.getString("secret");
            JSONObject payload = JSONObject.of("appId", appId, "clientSecret", secret);
            JSONObject response = RequestUtil.request(RequestUtil.post("https://bots.qq.com/app/getAppAccessToken").body(payload.toJSONString()));
            int code = response.getIntValue("code");
            if (code > 0) {
                log.error("Token获取失败: [{}]{}", code, response.getString("message"));
                return;
            }
            String token = response.getString("access_token");
            expires = response.getLongValue("expires_in") + DateUtil.nowTimeStamp();
            Api.setToken(token);
            map.put("expires", expires);
            log.info("Token获取成功");
        }
    }

}
