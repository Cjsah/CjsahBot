package net.cjsah.bot.event.events;

import net.cjsah.bot.Main;

public class AppReconnectEvent extends CancelableEvent {

    public AppReconnectEvent() {
        if (!Main.isRunning() || Main.isConnecting()) this.cancel();
    }
}
