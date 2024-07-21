package net.cjsah.bot.event.events;

import net.cjsah.bot.data.meta.HeartBeat;
import net.cjsah.bot.event.IEvent;

public record AppHeartBeatEvent(HeartBeat heartBeat) implements IEvent {

}
