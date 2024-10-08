package net.cjsah.bot.plugin;

import net.cjsah.bot.Signal;
import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.UserCommandSource;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.event.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class MainPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger("Console");
    public static MainPlugin INSTANCE = new MainPlugin();
    public static PluginInfo PLUGIN_INFO = new PluginInfo("main", "Main", "Main", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {
        CommandManager.register(MainPlugin.class);

        EventManager.subscribe(INSTANCE, MessageEvent.class, event -> {
            log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg());
        });

        EventManager.subscribe(INSTANCE, CommandEvent.class, event -> {
            UserCommandSource source = new UserCommandSource(event);
            CommandManager.execute(event.getCmdName(), event.getCmdOptions(), source);
        });

    }

    @Command("/botstop")
    public static void botStop(CommandSource<?> source) {
        source.sendFeedback("bot正在关闭...");
        Signal.stop();
    }

}
