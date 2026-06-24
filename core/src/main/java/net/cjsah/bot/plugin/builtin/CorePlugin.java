package net.cjsah.bot.plugin.builtin;

import cn.hutool.core.lang.Pair;
import com.google.gson.JsonNull;
import com.mojang.datafixers.util.Either;
import net.cjsah.bot.HeartBeatTimer;
import net.cjsah.bot.MainApplication;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.command.argument.StringArgument;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.data.AuthorInfo;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.BaseUserData;
import net.cjsah.bot.event.SubscribeEvent;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.FriendMessageSentEvent;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.event.events.GroupMessageSentEvent;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.loader.DummyClassLoader;
import net.cjsah.bot.packet.PacketHandler;
import net.cjsah.bot.packet.request.payload.GetFriendList;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.response.payload.FriendInfo;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContainer;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;
import net.cjsah.bot.plugin.entry.BuiltinPluginEntryPointImpl;
import net.cjsah.bot.plugin.registry.PluginRegistry;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        PluginRegistry registry = PluginManager.getRegistry();

        registry.permission().register(PermissionPlugin.EMPTY);

        registry.command().register(CorePlugin.class);

        registry.command().register(it ->
            it.literal("help")
                .description("查看帮助")
                .executes(context -> {
                    List<Pair<String, String>> help = Commands.getHelp("", context.getSource());
                    feedbackHelp(context.getSource(), help);
                    return 1;
                })
                .then(it.argument("cmd", StringArgument.greedyString())
                    .executes(context -> {
                        String command = StringArgument.get(context, "cmd").orElse("");
                        List<Pair<String, String>> help = Commands.getHelp(command, context.getSource());
                        feedbackHelp(context.getSource(), help);
                        return 1;
                    })
                )
        );

        registry.event().subscribe(this);
    }

    private static void feedbackHelp(CommandSource<?> source, List<Pair<String, String>> helps) {
        String content = helps.stream().map(it -> {
            StringBuilder builder = new StringBuilder();
            builder.append(it.getKey());
            String desc = it.getValue();
            if (desc != null && !desc.isEmpty()) {
                builder.append(" - ").append(desc);
            }
            return builder.toString();
        }).collect(Collectors.joining("\n"));
        source.sendFeedback(content);
    }

    @SimpleCommand(value = "/botstop", description = "关闭Bot", permission = UserRole.ADMIN)
    public static void botStop(CommandSource<?> source) {
        source.sendFeedback("bot正在关闭...");
        MainApplication.getInstance().halt();
    }

    @SimpleCommand(value = "/test", description = "测试", permission = UserRole.ADMIN)
    public static void test(CommandSource<?> source) {
        source.sendFeedback("111");
    }

    @SubscribeEvent
    private static void heartbeat(HeartbeatEvent event) {
        HeartBeatTimer.getInstance().heartbeatReceived(event.getWebSocketId(), event.getInterval());
    }

    @SubscribeEvent
    private static void friendMessage(FriendMessageEvent event) {
        BaseUserData sender = event.getSender();
        MainApplication.log.info("[{}] [{}({})] ==> {}", event.getMode().getText(), sender.getNickname(), sender.getUserId(), event.getMessage());
    }

    @SubscribeEvent
    private static void groupMessage(GroupMessageEvent event) {
        GroupUserData sender = event.getSender();
        MainApplication.log.info("[群聊] [{}({})] [{}({})] ==> {}", event.getGroupName(), event.getGroupId(), sender.getCard(), sender.getUserId(), event.getMessage());
    }

    @SubscribeEvent
    private static void friendMessageCallback(FriendMessageSentEvent event) {
        long id = event.getTargetId();
        RequestPacket packet = new GetFriendList();
        Either<List<FriendInfo>, String> res = PacketHandler.getInstance().send(packet);
        Stream<FriendInfo> stream = res.map(Collection::stream, it -> Stream.empty());
        String name = stream
            .filter(it -> it.userId() == id)
            .findFirst()
            .map(FriendInfo::getShowName)
            .orElse("Unknown");

        MainApplication.log.info("[{}] [{}({})] <== {}", event.getMode().getText(), name, id, event.getMessage());
    }

    @SubscribeEvent
    private static void GroupMessageCallback(GroupMessageSentEvent event) {
        MainApplication.log.info("[群聊] [{}({})] <== {}", event.getGroupName(), event.getGroupId(), event.getMessage());
    }

    @SubscribeEvent
    private static void commandTrigger(MessageEvent<?> event) {
        String message = event.getMessage().toString().trim();
        if (message.startsWith("/")) {
            CommandSource<?> source = event.getCommandSource();
            Commands.execute(source, message.substring(1));
        }
    }
}
