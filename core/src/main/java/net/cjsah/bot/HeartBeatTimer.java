package net.cjsah.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
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

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "HeartBeat")
public final class HeartBeatTimer {
    private static final HeartBeatTimer INSTANCE = new HeartBeatTimer();
    private static final int MAX_MISSES = 3;
    private static final long DEFAULT_INTERVAL = 30000L;

    private final Scheduler scheduler;
    private final Map<Long, BeatState> states;

    @SneakyThrows
    public HeartBeatTimer() {
        SchedulerFactory factory = new StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.states = new ConcurrentHashMap<>();
    }

    public static HeartBeatTimer getInstance() {
        return INSTANCE;
    }

    public void register(WebSocketClientImpl ws) {
        long wsId = ws.getId();
        BeatState state = new BeatState(ws);
        this.states.put(wsId, state);

        try {
            JobKey jobKey = jobKey(wsId);
            JobDetail job = JobBuilder
                .newJob(HeartBeatJob.class)
                .withIdentity(jobKey)
                .usingJobData("wsId", wsId)
                .build();
            Trigger trigger = newTrigger(wsId, DEFAULT_INTERVAL);

            this.scheduler.scheduleJob(job, trigger);
            if (!this.scheduler.isStarted()) {
                this.scheduler.start();
            }
            log.info("Registered heartbeat for ws-{} interval={}ms", wsId, DEFAULT_INTERVAL);
        } catch (SchedulerException e) {
            log.error("Failed to register heartbeat for ws-{}", wsId, e);
            this.states.remove(wsId);
        }
    }

    public void heartbeatReceived(long wsId, long interval) {
        BeatState state = this.states.get(wsId);
        if (state == null) return;

        state.misses.set(0);

        if (interval != 0 && state.interval != interval) {
            state.interval = interval;
            reschedule(wsId, interval);
        }
    }

    public void heartbeatReceived(long wsId) {
        heartbeatReceived(wsId, 0);
    }

    public void deregister(long wsId) {
        this.states.remove(wsId);
        try {
            this.scheduler.deleteJob(jobKey(wsId));
            log.info("Deregistered heartbeat for ws-{}", wsId);
        } catch (SchedulerException e) {
            log.error("Failed to deregister heartbeat for ws-{}", wsId, e);
        }
    }

    private void reschedule(long wsId, long intervalMs) {
        try {
            TriggerKey triggerKey = triggerKey(wsId);
            Trigger newTrigger = newTrigger(wsId, intervalMs);
            this.scheduler.rescheduleJob(triggerKey, newTrigger);
            log.debug("ws-{} heartbeat interval changed to {}ms", wsId, intervalMs);
        } catch (SchedulerException e) {
            log.error("Failed to reschedule heartbeat for ws-{}", wsId, e);
        }
    }

    private void checkMiss(long wsId) {
        BeatState state = this.states.get(wsId);
        if (state == null) return;

        if (state.misses.incrementAndGet() >= MAX_MISSES) {
            log.warn("ws-{} heartbeat lost after {} misses, closing connection", wsId, MAX_MISSES);
            deregister(wsId);
            state.ws.close();
        }
    }

    public void shutdown() {
        for (long wsId : this.states.keySet()) {
            deregister(wsId);
        }
        try {
            this.scheduler.shutdown(true);
        } catch (SchedulerException e) {
            log.error("Error shutting down scheduler", e);
        }
    }

    private static Trigger newTrigger(long wsId, long intervalMs) {
        return TriggerBuilder
            .newTrigger()
            .withIdentity(triggerKey(wsId))
            .startAt(new Date(System.currentTimeMillis() + intervalMs))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(intervalMs)
                .repeatForever())
            .build();
    }

    private static JobKey jobKey(long wsId) {
        return new JobKey("heartbeat-" + wsId);
    }

    private static TriggerKey triggerKey(long wsId) {
        return new TriggerKey("trigger-" + wsId);
    }

    private static final class BeatState {
        final WebSocketClientImpl ws;
        final AtomicInteger misses;
        volatile long interval;

        BeatState(WebSocketClientImpl ws) {
            this.ws = ws;
            this.misses = new AtomicInteger(0);
            this.interval = DEFAULT_INTERVAL;
        }
    }

    public static class HeartBeatJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            long wsId = context.getJobDetail().getJobDataMap().getLong("wsId");
            HeartBeatTimer.getInstance().checkMiss(wsId);
        }
    }
}
