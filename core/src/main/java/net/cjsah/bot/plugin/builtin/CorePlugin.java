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

//        Commands.registerContext().register(context -> {
//            return context.literal("test")
//                .executes(c -> {
//                    return 1;
//                })
//                .then(context.argument("arg1", AtArgument.atArg())
//                    .executes(c -> {
//                        Optional<Long> arg1 = AtArgument.get(c, "arg1");
//                        return 1;
//                    })
//                );
//        });
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
        int rp = getJrrp(String.valueOf(senderId));
        source.sendFeedback("您的今日人品为: %s".formatted(rp % 101));
    }

    @SimpleCommand(value = "/jrrp <qq:at>")
    public static void jrrp(CommandSource<?> source, long qq) {
        int rp = getJrrp(String.valueOf(qq));
        source.sendFeedback("%s的今日人品为: %s".formatted("", rp % 101));
    }

    private static long rol(long num, int k) {
        return (num << k) | (num >>> (64 - k));
    }

    private static long getHash(String str) {
        long num = 5381;
        for (int i = 0, len = str.length(); i < len; i++) {
            num = rol(num, 5) ^ num ^ str.charAt(i);
        }
        return num ^ 0xA9956E6B53C2E4EFL;
    }

    private static int getJrrp(String userId) {
        LocalDate now = LocalDate.now();
        int dayOfYear = now.getDayOfYear();
        int year = now.getYear();
        int dayOfMonth = now.getDayOfMonth();

        long hash1 = getHash("asdfgbn" + dayOfYear + "12#3$45" + year + "IUY");
        long hash2 = getHash("QWERTY" + userId + "0*8&6" + dayOfMonth + "kjhg");

        int num = (int) (Math.abs((hash1 / 3.0 + hash2 / 3.0) / 527.0) % 1001);
        if (num >= 970) return 100;
        return (int) Math.round(num / 969.0 * 99.0);
    }

}
