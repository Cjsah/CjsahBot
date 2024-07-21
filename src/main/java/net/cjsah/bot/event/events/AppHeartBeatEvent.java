package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.StatusData;
import net.cjsah.bot.event.IEvent;

@Getter
@RequiredArgsConstructor
public class AppHeartBeatEvent implements IEvent {
    private final long interval;
    private final StatusData status;
}
