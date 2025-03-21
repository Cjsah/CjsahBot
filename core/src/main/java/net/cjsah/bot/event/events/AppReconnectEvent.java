package net.cjsah.bot.event.events;

import net.cjsah.bot.MainApplication;

public class AppReconnectEvent extends CancelableEvent {

    public AppReconnectEvent() {
        if (!MainApplication.isRunning() || MainApplication.isConnecting()) this.cancel();
    }
}
