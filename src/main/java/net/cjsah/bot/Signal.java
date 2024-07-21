package net.cjsah.bot;

import lombok.Getter;
import lombok.Setter;
import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.events.AppStopEvent;

public class Signal {
    @Getter
    @Setter
    private static boolean Stop = false;
    @Getter
    @Setter
    public static boolean AppInit = false;
    @Setter
    @Getter
    public static boolean AppEnable = false;
    @Setter
    @Getter
    public static boolean AppGood = false;
    @Setter
    @Getter
    public static boolean Online = false;
    @Setter
    @Getter
    public static boolean Good = false;

    public static void stop() {
        AppStopEvent event = new AppStopEvent();
        Event.broadcast(event);
        if (!event.isCancel()) {
            Stop = true;
        }
    }

}
