package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import net.cjsah.bot.util.CodecUtil;

public class AppStopEvent extends CancelableEvent {
    public static final Codec<AppStopEvent> CODEC = CodecUtil.supplier(AppStopEvent::new);
}
