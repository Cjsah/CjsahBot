package net.cjsah.bot.event.events;

import net.cjsah.bot.data.HeartBeat;
import net.cjsah.bot.event.IEvent;

public record AppHeartBeatEvent(HeartBeat heartBeat) implements IEvent {

}
