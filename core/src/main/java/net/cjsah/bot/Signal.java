package net.cjsah.bot;

import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.AppStopEvent;

public class Signal {
    private static boolean Stop = false;

    public static void stop() {
        AppStopEvent event = new AppStopEvent();
        EventManager.broadcast(event);
        if (!event.isCancel()) {
            Stop = true;
            Main.sendSignal(SignalType.STOP);
        }
    }

    public static boolean isRunning() {
        return !Stop;
    }
}
