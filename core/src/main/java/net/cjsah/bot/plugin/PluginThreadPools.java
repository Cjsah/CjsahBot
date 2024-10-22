package net.cjsah.bot.plugin;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class PluginThreadPools {
    private static final ExecutorService Executor = Executors.newCachedThreadPool();
    private static final Map<String, PluginThread> Threads = new ConcurrentHashMap<>();

    public static synchronized void execute(String pluginId, Runnable runnable) {
        PluginThread thread = Threads.get(pluginId);
        if (thread == null) {
            thread = new PluginThread(pluginId);
            Threads.put(pluginId, thread);
            Executor.submit(thread);
        }
        thread.submitTask(runnable);
    }

    public static synchronized void execute(Runnable runnable) {
        PluginInfo info = PluginContext.PLUGIN_INFO.get();
        if (info == null) info = MainPlugin.PLUGIN_INFO;
        PluginThreadPools.execute(info.getId(), runnable);
    }

    public static synchronized void unloadPlugin(String pluginId) {
        PluginThread thread = Threads.get(pluginId);
        if (thread != null) {
            PluginInfo info = PluginContext.getPluginInfo(pluginId);
            assert info != null;
            thread.terminate();
            try {
                synchronized (thread.lock) {
                    if (!thread.cancelled) {
                        thread.lock.wait();
                    }
                }
                PluginContext.PluginData data = PluginContext.removePlugin(pluginId);
                if (data == null || data.loader() == null) return;
                try {
                    data.loader().close();
                } catch (IOException e) {
                    PluginThread.log.error("Cannot close plugin: {}", info.getId(), e);
                }
            } catch (InterruptedException e) {
                PluginThread.log.error("Plugin thread was interrupted", e);
            } catch (Exception e) {
                PluginThread.log.error("Plugin {} thread exception", info.getId(), e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void awaitShutdown() throws InterruptedException {
        Executor.shutdown();
        Executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static class PluginThread implements Runnable {
        private static final Logger log = LoggerFactory.getLogger("PluginThread");
        private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
        private volatile boolean running = true;
        private volatile boolean cancelled = false;
        protected final Object lock = new Object();
        private final String pluginId;

        public PluginThread(String pluginId) {
            this.pluginId = pluginId;
        }

        @Override
        public void run() {
            Plugin plugin = PluginContext.getPlugin(this.pluginId);
            PluginContext.PLUGIN.set(plugin);
            while (this.running || !this.tasks.isEmpty()) {
                try {
                    Runnable task = tasks.take();
                    task.run();
                } catch (InterruptedException e) {
                    log.error("Plugin thread interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
            PluginContext.PLUGIN.remove();
            PluginContext.PLUGIN_INFO.remove();
            this.cancelled = true;
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
        }

        public synchronized void submitTask(@NotNull Runnable task) {
            if (this.running) {
                tasks.offer(task);
            } else {
                log.warn("Plugin thread is still running");
            }
        }

        public void terminate() {
            running = false;
            tasks.offer(() -> {}); // wake up the thread if waiting
        }
    }
}
