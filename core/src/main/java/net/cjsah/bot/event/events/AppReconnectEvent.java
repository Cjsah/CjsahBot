package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import net.cjsah.bot.AppInstantStatus;
import net.cjsah.bot.MainApplication;
import net.cjsah.bot.util.CodecUtil;

public class AppReconnectEvent extends CancelableEvent {

    public static final Codec<AppReconnectEvent> CODEC = CodecUtil.supplier(AppReconnectEvent::new);

    public AppReconnectEvent() {
        AppInstantStatus status = MainApplication.getInstance().getStatus();
        if (!status.app().isRunning() || !status.websocket().isConnected()) {
            this.cancel();
        }
    }

}
