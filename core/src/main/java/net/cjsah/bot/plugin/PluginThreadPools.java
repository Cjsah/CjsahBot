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

public class PluginThreadPools {
    private static final ExecutorService Executor = Executors.newCachedThreadPool();
    private static final Map<Plugin, PluginThread> Threads = new ConcurrentHashMap<>();

    public static synchronized void execute(Plugin plugin, Runnable runnable) {
        PluginThread thread = Threads.get(plugin);
        if (thread == null) {
            thread = new PluginThread(plugin);
            Threads.put(plugin, thread);
            Executor.submit(thread);
        }
        thread.submitTask(runnable);
    }

    public static synchronized void execute(Runnable runnable) {
        Plugin plugin = PluginContext.PLUGIN.get();
        if (plugin == null) plugin = MainPlugin.INSTANCE;
        execute(plugin, runnable);
    }

    public static synchronized void unloadPlugin(Plugin plugin) {
        PluginThread thread = Threads.get(plugin);
        if (thread != null) {
            PluginInfo info = PluginContext.getPluginInfo(plugin);
            assert info != null;
            thread.terminate();
            try {
                synchronized (thread.lock) {
                    if (!thread.cancelled) {
                        thread.lock.wait();
                    }
                }
                PluginContext.PluginData data = PluginContext.removePlugin(plugin);
                if (data == null || data.loader() == null) return;
                try {
                    data.loader().close();
                } catch (IOException e) {
                    PluginThread.log.error("Cannot close plugin: {}", info.getId(), e);
                }
            } catch (InterruptedException e) {
                PluginThread.log.error("Plugin thread was interrupted", e);
            }
        }
    }

    public static void shutdown() {
        Executor.shutdown();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static class PluginThread implements Runnable {
        private static final Logger log = LoggerFactory.getLogger("PluginThread");
        private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
        private volatile boolean running = true;
        private volatile boolean cancelled = false;
        protected final Object lock = new Object();
        private final Plugin plugin;

        public PluginThread(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            PluginContext.PLUGIN.set(this.plugin);
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
