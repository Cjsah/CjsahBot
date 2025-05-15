package net.cjsah.bot;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class HeartBeatTimer {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatTimer.class);
    private final Scheduler scheduler;
    private final AtomicInteger count;
    private final AtomicBoolean lifecycle;
    private final JobDataMap jobMap;

    public HeartBeatTimer() throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.count = new AtomicInteger(0);
        this.lifecycle = new AtomicBoolean(false);
        this.jobMap = new JobDataMap();
        this.jobMap.put("this", this);
    }

    private void waitLifeCycle() throws SchedulerException {
        if (lifecycle.get()) return;
        JobDetail job = JobBuilder
                .newJob(LifeCycleJob.class)
                .withIdentity("Job", "LifeCycle")
                .usingJobData(this.jobMap)
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("Trigger", "LifeCycle")
                .startAt(new Date(System.currentTimeMillis() + 3000L))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();
        this.scheduler.scheduleJob(job, trigger);
    }

    private void appendHeartJob(long time) throws SchedulerException {
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
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(time).repeatForever())
                .build();
        this.scheduler.pauseTrigger(triggerKey);
        this.scheduler.deleteJob(jobKey);
        this.scheduler.scheduleJob(job, trigger);
    }

    public void start() throws SchedulerException {
        this.waitLifeCycle();
        this.appendHeartJob(30000L);
        this.count.set(0);
        if (!this.scheduler.isStarted()) {
            this.scheduler.start();
        }
    }

    public synchronized void stop() {
        TriggerKey triggerKey = new TriggerKey("Trigger", "Heart");
        JobKey heartJobKey = new JobKey("Job", "Heart");
        JobKey lifecycleJobKey = new JobKey("Job", "LifeCycle");
        try {
            this.scheduler.pauseTrigger(triggerKey);
            this.scheduler.deleteJob(lifecycleJobKey);
            this.scheduler.deleteJob(heartJobKey);
        } catch (SchedulerException e) {
            log.error("Error stopping heartbeat timer", e);
        }
    }

    public void lifecycle() {
        this.lifecycle.set(true);
        try {
            JobKey jobKey = new JobKey("Job", "LifeCycle");
            this.scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("Error stopping lifeCycle timer", e);
        }
    }

    public void heart(long nextTime) {
        try {
            this.appendHeartJob(nextTime + 1000L);
            this.count.set(0);
        } catch (SchedulerException e) {
            log.error("Error stopping lifeCycle timer", e);
        }
    }

    private synchronized void heartCheck() {
        if (this.count.incrementAndGet() >= 3) {
            this.stop();
            Main.sendSignal(SignalType.RE_CONNECT);
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
            timer.heartCheck();
        }
    }

    public static class LifeCycleJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            HeartBeatTimer timer = (HeartBeatTimer) map.get("this");
            if (timer.lifecycle.get()) return;
            log.warn("未收到生命周期事件, 连接已丢失.");
            timer.stop();
            Main.sendSignal(SignalType.RE_CONNECT);
        }
    }
}
