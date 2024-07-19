package net.cjsah.bot;

import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.events.AppStopEvent;

public class Signal {
    private static boolean Stop = false;
    public static boolean AppInit = false;
    public static boolean AppEnable = false;
    public static boolean AppGood = false;
    public static boolean Online = false;
    public static boolean Good = false;

    public static boolean isStop() {
        return Stop;
    }

    public static void stop() {
        AppStopEvent event = new AppStopEvent();
        Event.broadcast(event);
        if (!event.isCancel()) {
            Stop = true;
        }
    }

    public static boolean isAppInit() {
        return AppInit;
    }

    public static void setAppInit(boolean appInit) {
        AppInit = appInit;
    }

    public static boolean isAppEnable() {
        return AppEnable;
    }

    public static void setAppEnable(boolean appEnable) {
        AppEnable = appEnable;
    }

    public static boolean isAppGood() {
        return AppGood;
    }

    public static void setAppGood(boolean appGood) {
        AppGood = appGood;
    }

    public static boolean isOnline() {
        return Online;
    }

    public static void setOnline(boolean online) {
        Online = online;
    }

    public static boolean isGood() {
        return Good;
    }

    public static void setGood(boolean good) {
        Good = good;
    }
}
