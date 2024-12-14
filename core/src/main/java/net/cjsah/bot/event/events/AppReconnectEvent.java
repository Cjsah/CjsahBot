package net.cjsah.bot.event.events;

import net.cjsah.bot.Main;
import net.cjsah.bot.event.CancelableEvent;

public class AppReconnectEvent extends CancelableEvent {

    public AppReconnectEvent() {
        if (Main.isConnecting()) this.cancel();
    }
}
