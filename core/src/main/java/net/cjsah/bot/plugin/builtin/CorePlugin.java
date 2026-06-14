package net.cjsah.bot.plugin.builtin;

import com.google.gson.JsonNull;
import net.cjsah.bot.HeartBeatTimer;
import net.cjsah.bot.MainApplication;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.data.AuthorInfo;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.BaseUserData;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.events.LifecycleEvent;
import net.cjsah.bot.loader.DummyClassLoader;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContainer;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.BuiltinPluginEntryPointImpl;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public final class CorePlugin implements Plugin {
    public static final PluginContainer INSTANCE;

    static {
       PluginMetadata metadata = new PluginMetadata(
           "core", "2.0.0", "Core", "Builtin Core Plugin",
           List.of(new AuthorInfo("Cjsah", Optional.of(""))),
           "Builtin " + CorePlugin.class.getName(),
           JsonNull.INSTANCE
       );
       INSTANCE = new PluginContainer(
           metadata,
           Paths.get("."),
           new BuiltinPluginEntryPointImpl(new CorePlugin()),
           new DummyClassLoader()
       );
   }

    @Override
    public void load() {
        Commands.registerContext().register(CorePlugin.class);

        String pluginId = INSTANCE.id();

//        @Deprecated
//        EventManager.subscribe(pluginId, MessageEvent.class, event ->
//                log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg())
//        );

//        EventManager.subscribe(pluginId, MessageEmojiPinEvent.class, event -> {
//            log.info("{} {} {} {}", event.getUserId(), event.getEmoji(), event.getChannelId(), event.getMsgId());
//        });

        EventManager.subscribe(pluginId, LifecycleEvent.class, event -> {
//            if (event.getStatus() == LifecycleEvent.Status.CONNECT) {
//                Main.lifecycle(false, 0);
//            }
        });

        EventManager.subscribe(pluginId, HeartbeatEvent.class, event -> {
            HeartBeatTimer.getInstance().heartbeatReceived(event.getWebSocketId(), event.getInterval());
        });

        EventManager.subscribe(pluginId, FriendMessageEvent.class, event -> {
            BaseUserData sender = event.getSender();
            MainApplication.log.info("[{}] [{}({})] => {}", event.getMode().getType(), sender.getNickname(), sender.getUserId(), event.getMessage());
        });

        EventManager.subscribe(pluginId, GroupMessageEvent.class, event -> {
            GroupUserData sender = event.getSender();
//            log.info("[群聊] [{}({})] [{}({})] => {}", event.getGroupName(), event.getGroupId(), sender.getCard(), sender.getUserId(), event.getMessage());
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

    @SimpleCommand(value = "/botstop", permission = UserRole.ADMIN)
    public static void botStop(CommandSource<?> source) {
        source.sendFeedback("bot正在关闭...");
        MainApplication.getInstance().halt();
    }

    @SimpleCommand(value = "/test", permission = UserRole.OWNER)
    public static void test(CommandSource<?> source) {
    }

}
