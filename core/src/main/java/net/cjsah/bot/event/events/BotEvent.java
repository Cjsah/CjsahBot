package net.cjsah.bot.event.events;

public abstract class BotEvent extends Event {
    protected final String eventId;

    public BotEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return this.eventId;
    }
}
