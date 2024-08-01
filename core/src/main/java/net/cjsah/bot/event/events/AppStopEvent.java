package net.cjsah.bot.event.events;

import net.cjsah.bot.event.IEvent;

public class AppStopEvent implements IEvent {

    private boolean isCancel = false;

    @Override
    public boolean cancelable() {
        return true;
    }

    @Override
    public boolean isCancel() {
        return this.isCancel;
    }

    @Override
    public void cancel() {
        this.isCancel = true;
    }

}
