package net.cjsah.bot.plugin;

import lombok.extern.slf4j.Slf4j;

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

    public synchronized void unloadPlugin(Plugin plugin) {
        PluginThread thread = Threads.get(plugin);
        if (thread != null) {
            thread.terminate();
            try {
                thread.lock.wait();
                PluginContext.PluginData data = PluginContext.PLUGIN_MAP.remove(plugin);
                PluginContext.PLUGINS.remove(data.info().getId());
            } catch (InterruptedException e) {
                PluginThread.log.error("Plugin thread was interrupted", e);
            }
        }
    }

    public static void shutdown() {
        Executor.shutdown();
    }

    @Slf4j
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static class PluginThread implements Runnable {
        private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
        private volatile boolean running = true;
        protected final Object lock = new Object();
        private final Plugin plugin;

        public PluginThread(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            PluginContext.PLUGIN.set(this.plugin);
            while (this.running) {
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
            this.lock.notifyAll();
        }

        public synchronized void submitTask(Runnable task) {
            tasks.offer(task);
        }

        public void terminate() {
            running = false;
            tasks.offer(() -> {}); // wake up the thread if waiting
        }
    }
}
