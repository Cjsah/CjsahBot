package net.cjsah.bot;

import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.AppStopEvent;

public class Signal {
    public static final Object StopLock = new Object();

    private static boolean Stop = false;

    public static void stop() {
        AppStopEvent event = new AppStopEvent();
        EventManager.broadcast(event);
        if (!event.isCancel()) {
            Stop = true;
            synchronized (StopLock) {
                StopLock.notifyAll();
            }
        }
    }

    public static void waitStop() throws InterruptedException {
        synchronized (StopLock) {
            StopLock.wait();
        }
    }

    public static boolean isRunning() {
        return !Stop;
    }
}
