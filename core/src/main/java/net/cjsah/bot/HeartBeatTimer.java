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

import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatTimer {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatTimer.class);
    private final Scheduler scheduler;
    private final AtomicInteger count;
    private final JobDataMap jobMap;
    private final Runnable heartTask;
    private final Runnable failTask;

    public HeartBeatTimer(Runnable heart, Runnable fail) throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.count = new AtomicInteger(0);
        this.jobMap = new JobDataMap();
        this.jobMap.put("this", this);
        this.heartTask = heart;
        this.failTask = fail;
    }

    public void start() throws SchedulerException {
        JobDetail job = JobBuilder
                .newJob(HeartBeatJob.class)
                .withIdentity("Job", "Heart")
                .usingJobData(this.jobMap)
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("Trigger", "Heart")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
                .build();
        this.scheduler.scheduleJob(job, trigger);
        this.count.set(0);
        if (!this.scheduler.isStarted()) {
            this.scheduler.start();
        }
    }

    public synchronized void stop() {
        TriggerKey triggerKey = new TriggerKey("Trigger", "Heart");
        JobKey jobKey = new JobKey("Job", "Heart");
        try {
            this.scheduler.pauseTrigger(triggerKey);
            this.scheduler.unscheduleJob(triggerKey);
            this.scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("Error stopping heartbeat timer", e);
        }
    }

    private synchronized void heartPing() {
        this.heartTask.run();
        int count = this.count.incrementAndGet();
        if (count >= 3) {
            this.stop();
            this.failTask.run();
        }
    }

    public void cancel() {
        try {
            this.scheduler.shutdown(true);
        } catch (SchedulerException e) {
            log.error("Error stopping heartbeat timer", e);
        }
    }

    public synchronized void heartPong() {
        this.count.set(0);
    }

    public static class HeartBeatJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            HeartBeatTimer timer = (HeartBeatTimer) map.get("this");
            timer.heartPing();
        }
    }
}
