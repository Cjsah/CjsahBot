package net.cjsah.bot.plugin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PluginExecutor {
    public static final Runnable STOP = () -> {
        throw new RuntimeException("Stop task is no need execute");
    };

    private final PluginContainer plugin;
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private final Thread workerThread;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final ScopedValue.Carrier carrier;

    public PluginExecutor(ScopedValue<PluginContainer> context, PluginContainer plugin) {
        this.plugin = plugin;
        this.carrier = ScopedValue.where(context, plugin);
        this.workerThread = Thread.ofPlatform()
            .daemon(false)
            .name("plugin-worker-" + plugin.id())
            .start(this::run);
    }

    public void run() {
        try {
            while (true) {
                Runnable task = this.tasks.take();
                if (task == STOP) {
                    break;
                }
                this.executor.execute(() -> this.carrier.run(task));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.executor.shutdown();
            try {
                this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean submit(Runnable runnable) {
        if (this.shutdown.get()) {
            this.plugin.log().warn("PluginExecutor is shutdown");
            return false;
        }
        this.tasks.add(runnable);
        return true;
    }

    public ScopedValue.Carrier getCarrier() {
        return this.carrier;
    }

    public void awaitTermination() throws InterruptedException {
        this.workerThread.join();
    }

    public void shutdown() {
        if (this.shutdown.compareAndSet(false, true)) {
            this.tasks.add(STOP);
        }
    }

    public void shutdown(boolean await) {
        this.shutdown();
        if (await) {
            try {
                this.awaitTermination();
            } catch (InterruptedException ignored) {}
        }
    }
}
