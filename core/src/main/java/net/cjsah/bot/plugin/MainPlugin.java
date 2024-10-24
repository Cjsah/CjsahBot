package net.cjsah.bot.plugin;

import net.cjsah.bot.Main;
import net.cjsah.bot.SignalType;
import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public final class MainPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger("Console");
    public static final MainPlugin INSTANCE = new MainPlugin();
    public static final PluginInfo PLUGIN_INFO = new PluginInfo("main", "Main", "Main", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {
        CommandManager.register(MainPlugin.class);

        String pluginId = PLUGIN_INFO.getId();

//        @Deprecated
//        EventManager.subscribe(pluginId, MessageEvent.class, event ->
//                log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg())
//        );

        EventManager.subscribe(pluginId, CommandEvent.class, event -> {
            log.info("执行命令: [{}] [{}] [{}({})] => /{}",
                    event.getRoomInfo().getName(),
                    event.getChannelInfo().getName(),
                    event.getSenderInfo().getNickname(),
                    event.getSenderInfo().getId(),
                    event.getCommandInfo().getCommand()
            );
            CommandSource source = new CommandSource(event);
            CommandManager.execute(event.getCommandInfo(), source);
        });
    }

    @Command(value = "/botstop", permissions = Permission.ADMIN)
    public static void botStop(CommandSource source) {
        source.sendFeedback("bot正在关闭...");
        Main.sendSignal(SignalType.STOP);
    }

}
