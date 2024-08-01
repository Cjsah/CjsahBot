package net.cjsah.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.AppStopEvent;

@Getter
@Setter
public class Signal {
    @Setter(AccessLevel.NONE)
    public static final Object StopLock = new Object();

    private static boolean Stop = false;
    public static boolean AppInit = false;
    public static boolean AppEnable = false;
    public static boolean AppGood = false;
    public static boolean Online = false;
    public static boolean Good = false;

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

    @SneakyThrows
    public static void waitStop() {
        synchronized (StopLock) {
            StopLock.wait();
        }
    }

    public static boolean isRunning() {
        return !Stop;
    }

    public static void fromStatus(StatusData status) {
        AppInit = status.isAppInitialized();
        AppEnable = status.isAppEnabled();
        AppGood = status.isAppGood();
        Online = status.isOnline();
        Good = status.isGood();
    }

}
