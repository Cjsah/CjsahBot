package net.cjsah.bot.plugin;

import net.cjsah.bot.Main;
import net.cjsah.bot.SignalType;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.UserData;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.events.LifecycleEvent;
import net.cjsah.bot.permission.PermissionRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public final class MainPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger("Console");
    public static final MainPlugin INSTANCE = new MainPlugin();
    public static final PluginInfo PLUGIN_INFO = new PluginInfo("core", "Core", "Core Plugin", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {
        Commands.registerContext().register(MainPlugin.class);

        String pluginId = PLUGIN_INFO.getId();

//        @Deprecated
//        EventManager.subscribe(pluginId, MessageEvent.class, event ->
//                log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg())
//        );

//        EventManager.subscribe(pluginId, MessageEmojiPinEvent.class, event -> {
//            log.info("{} {} {} {}", event.getUserId(), event.getEmoji(), event.getChannelId(), event.getMsgId());
//        });

        EventManager.subscribe(pluginId, LifecycleEvent.class, event -> {
            if (event.getStatus() == LifecycleEvent.Status.CONNECT) {
                Main.lifecycle(false, 0);
            }
        });

        EventManager.subscribe(pluginId, HeartbeatEvent.class, event -> Main.lifecycle(true, event.getInterval()));

        EventManager.subscribe(pluginId, FriendMessageEvent.class, event -> {
            UserData sender = event.getSender();
            log.info("[{}] [{}({})] => {}", event.getMode().getType(), sender.getNickname(), sender.getUserId(), event.getMessage());
        });

        EventManager.subscribe(pluginId, GroupMessageEvent.class, event -> {
            GroupUserData sender = event.getSender();
            log.info("[群聊] [{}({})] [{}({})] => {}", event.getGroupName(), event.getGroupId(), sender.getCard(), sender.getUserId(), event.getMessage());
        });

//        EventManager.subscribe(pluginId, CommandEvent.class, event -> {
////            log.info("[{}({})] [{}({})] ==> 触发命令: /{}",
////                    event.getRoomInfo().getName(),
////                    event.getRoomInfo().getId(),
////                    event.getSenderInfo().getNickname(),
////                    event.getSenderInfo().getId(),
////                    event.getCommandInfo().getCommand()
////            );
//            CommandSource source = new CommandSource(event);
//            CommandManager.execute(event.getCommandInfo(), source);
//        });
    }

    @SimpleCommand(value = "/botstop", permission = PermissionRole.ADMIN)
    public static void botStop(CommandSource<?> source) {
        source.sendFeedback("bot正在关闭...");
        Main.sendSignal(SignalType.STOP);
    }

    @SimpleCommand(value = "/test", permission = PermissionRole.OWNER)
    public static void test(CommandSource<?> source) {
        Main.sendSignal(SignalType.RESTART);
    }

}
