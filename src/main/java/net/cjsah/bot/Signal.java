package net.cjsah.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.events.AppStopEvent;

@Getter
@Setter
public class Signal {
    @Setter(AccessLevel.NONE)
    private static boolean Stop = false;
    public static boolean AppInit = false;
    public static boolean AppEnable = false;
    public static boolean AppGood = false;
    public static boolean Online = false;
    public static boolean Good = false;

    public static void stop() {
        AppStopEvent event = new AppStopEvent();
        Event.broadcast(event);
        if (!event.isCancel()) {
            Stop = true;
        }
    }

    public static boolean isStopped() {
        return Stop;
    }

    public static void fromStatus(StatusData status) {
        AppInit = status.isAppInitialized();
        AppEnable = status.isAppEnabled();
        AppGood = status.isAppGood();
        Online = status.isOnline();
        Good = status.isGood();
    }

}
