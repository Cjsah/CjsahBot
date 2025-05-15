package net.cjsah.bot;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.type.Opcode;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class HeartBeatTimer {
    private static final Logger log = LoggerFactory.getLogger("HeartBeat");
    private final Scheduler scheduler;
    private final Consumer<JSONObject> sendFunc;
    private final AtomicInteger count;
    private final JobDataMap jobMap;
    private long heartTime;

    public HeartBeatTimer(Scheduler scheduler, Consumer<JSONObject> send) {
        this.scheduler = scheduler;
        this.count = new AtomicInteger(0);
        this.jobMap = new JobDataMap();
        this.jobMap.put("this", this);
        this.sendFunc = send;
        this.heartTime = 0;
    }

    public void start() throws SchedulerException {
        log.debug("正在启动心跳服务...");
        TriggerKey triggerKey = new TriggerKey("Trigger", "Heart");
        JobKey jobKey = new JobKey("Job", "Heart");
        JobDetail job = JobBuilder
                .newJob(HeartBeatJob.class)
                .withIdentity(jobKey)
                .usingJobData(this.jobMap)
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(this.heartTime).repeatForever())
                .build();
        this.scheduler.pauseTrigger(triggerKey);
        this.scheduler.deleteJob(jobKey);
        this.scheduler.scheduleJob(job, trigger);
        this.count.set(0);
        if (!this.scheduler.isStarted()) {
            this.scheduler.start();
        }
    }

    public synchronized void stop() {
        TriggerKey triggerKey = new TriggerKey("Trigger", "Heart");
        JobKey heartJobKey = new JobKey("Job", "Heart");
        try {
            if (this.scheduler.isShutdown()) return;
            this.scheduler.pauseTrigger(triggerKey);
            this.scheduler.deleteJob(heartJobKey);
        } catch (SchedulerException e) {
            log.error("Error stopping heartbeat timer", e);
        }
    }

    public void setHeartTime(long time) {
        this.heartTime = time;
    }

    public synchronized void hearted() {
        log.debug("收到心跳, 当前计数器: {}", this.count.get());
        this.count.set(0);
    }

    private synchronized void heartbeat() {
        log.debug("发送心跳, 当前计数器: {}", this.count.get());
        if (this.count.incrementAndGet() >= 3) {
            log.warn("连接断开: 心跳超时");
            this.stop();
            MainApplication.sendSignal(SignalType.RE_CONNECT);
        } else {
            JSONObject payload = Opcode.HEARTBEAT.generate(true, null);
            this.sendFunc.accept(payload);
        }
    }

    public void cancel() {
        try {
            this.scheduler.shutdown(true);
        } catch (SchedulerException e) {
            log.error("Error stopping heartbeat timer", e);
        }
    }

    public static class HeartBeatJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            HeartBeatTimer timer = (HeartBeatTimer) map.get("this");
            timer.heartbeat();
        }
    }
}
