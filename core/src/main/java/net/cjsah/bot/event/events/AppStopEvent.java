package net.cjsah.bot.event.events;

import net.cjsah.bot.event.Event;
import net.cjsah.bot.event.ICancelable;

public class AppStopEvent extends Event implements ICancelable {

    private boolean isCancel = false;

    @Override
    public boolean isCancel() {
        return this.isCancel;
    }

    @Override
    public void cancel() {
        this.isCancel = true;
    }
}
