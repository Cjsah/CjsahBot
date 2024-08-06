import lombok.SneakyThrows;
import net.cjsah.bot.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.quartz.DateBuilder;
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

import java.util.function.Supplier;

public class JsonTest {

    @Test
    public void run() throws SchedulerException, InterruptedException {
//        TestTimer timer = new TestTimer();
//
//        TimeUnit.SECONDS.sleep(7);
//        timer.heartbeat(7000);
//
//
//
//
//        TimeUnit.MINUTES.sleep(2);
        String url = "https://a.b.c/download?1=2&3=4&amp;5=6&aa;7=8&&t&scioaschioashc&oa&;hosi&c&";
        System.out.println(url);
        System.out.println(StringUtil.netReplace(url));

    }

    public static class TestTimer {
        private final Scheduler scheduler;
        private final JobDataMap jobMap = new JobDataMap();
        private final Runnable task;

        private int count = 0;

        public TestTimer() throws SchedulerException {
            SchedulerFactory factory = new StdSchedulerFactory();
            this.scheduler = factory.getScheduler();
            this.scheduler.start();
            this.jobMap.put("this", this);
            JobDetail job = JobBuilder
                    .newJob(TestJob.class)
                    .withIdentity("TestJob", "Main")
                    .usingJobData(this.jobMap)
                    .build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("TestTrigger", "Main")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                    .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND))
                    .build();
            this.scheduler.scheduleJob(job, trigger);
            this.task = () -> {
                System.out.println(1);
            };
        }

        public void timeout() throws Exception {
            System.out.println(System.currentTimeMillis() / 1000);
            System.out.println("timeout");
            System.out.println(this.count + 1);
            if (++this.count >= 3) {
                this.reschedule(() -> {
                    this.task.run();
                    return 5000;
                });
            }
            System.out.println("===");
        }

        private void reschedule(Supplier<Integer> triggerTime) throws SchedulerException {
            TriggerKey triggerKey = new TriggerKey("TestTrigger", "Main");
            JobKey jobKey = new JobKey("TestJob", "Main");
            this.scheduler.pauseTrigger(triggerKey);
            this.scheduler.unscheduleJob(triggerKey);
            this.scheduler.deleteJob(jobKey);
            this.count = 0;
            int time = triggerTime.get();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(time).repeatForever())
                    .startAt(DateBuilder.futureDate(time, DateBuilder.IntervalUnit.MILLISECOND))
                    .build();
            JobDetail job = JobBuilder.newJob(TestJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(this.jobMap)
                    .build();
            this.scheduler.scheduleJob(job, trigger);
        }

        public void heartbeat(long interval) throws SchedulerException {
            System.out.println(System.currentTimeMillis() / 1000);
            System.out.println("heart");
            this.reschedule(() -> (int) interval);
            System.out.println("===");
        }

    }

    public static class TestJob implements Job {

        @SneakyThrows
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            TestTimer timer = (TestTimer) map.get("this");
            timer.timeout();
        }
    }
}
