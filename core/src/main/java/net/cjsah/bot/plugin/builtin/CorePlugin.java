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
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.loader.DummyClassLoader;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContainer;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.BuiltinPluginEntryPointImpl;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

        EventManager.subscribe(pluginId, MessageEvent.class, event -> {
            String message = event.getMessage().toString().trim();
            if (message.startsWith("/")) {
                CommandSource<?> source = event.getCommandSource();
                Commands.execute(source, message.substring(1));
            }
        });

        EventManager.subscribe(pluginId, HeartbeatEvent.class, event -> {
            HeartBeatTimer.getInstance().heartbeatReceived(event.getWebSocketId(), event.getInterval());
        });

        EventManager.subscribe(pluginId, FriendMessageEvent.class, event -> {
            BaseUserData sender = event.getSender();
            MainApplication.log.info("[{}] [{}({})] => {}", event.getMode().getText(), sender.getNickname(), sender.getUserId(), event.getMessage());
        });

        EventManager.subscribe(pluginId, GroupMessageEvent.class, event -> {
            GroupUserData sender = event.getSender();
            MainApplication.log.info("[群聊] [{}({})] [{}({})] => {}", event.getGroupName(), event.getGroupId(), sender.getCard(), sender.getUserId(), event.getMessage());
        });
    }

    @SimpleCommand(value = "/botstop", permission = UserRole.ADMIN)
    public static void botStop(CommandSource<?> source) {
        source.sendFeedback("bot正在关闭...");
        MainApplication.getInstance().halt();
    }

    @SimpleCommand(value = "/test abc")
    public static void test(CommandSource<?> source) {
        source.sendFeedback("test");
    }

    @SimpleCommand(value = "/jrrp")
    public static void jrrp(CommandSource<?> source) {
        long senderId = source.getSender().getUserId();
        jrrp(source, senderId);
    }

    @SimpleCommand(value = "/jrrp <qq:at>")
    public static void jrrp(CommandSource<?> source, long qq) {
        System.out.println(qq);
        int rp = getRp(qq);
        source.sendFeedback("您的今日人品为: %s".formatted(rp % 101));
    }

    public static int getRp(long senderId) {
        if (senderId == 2684117397L) {
            return 100;
        }
        if (senderId == 1270399267L) {
            return -1;
        }
        long seed = LocalDate.now().toEpochDay() ^ (senderId * 0x9E3779B97F4A7C15L);
        return new Random(seed).nextInt(101) % 101;
    }

}
