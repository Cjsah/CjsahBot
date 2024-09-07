package net.cjsah.bot.plugin;

import net.cjsah.bot.Signal;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.ConsoleCommandSource;
import net.cjsah.bot.command.source.UserCommandSource;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.permission.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger("Console");
    public static MainPlugin INSTANCE = new MainPlugin();
    public static PluginInfo PLUGIN_INFO = new PluginInfo("main", "Main", "Main", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {

        EventManager.subscribe(INSTANCE, MessageEvent.class, event -> {
            log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg());
        });

        EventManager.subscribe(INSTANCE, CommandEvent.class, event -> {
            String cmd;
            if ("/cmd".equals(event.getCmdName())) {
                cmd = event.getCmdOptions().getFirst().value();
            } else {
                List<String> nodes = event.getCmdOptions().stream().map(CommandEvent.CommandNode::value).toList();
                cmd = event.getCmdName().substring(1) + " " + String.join(" ", nodes);
            }
            UserCommandSource source = new UserCommandSource(event);
            CommandManager.execute(cmd.trim(), source);
        });

        CommandManager.register(CommandManager.literal("console").requires(it -> it.hasPermission(RoleType.ADMIN)).then(CommandManager.literal("stop").executes("关闭Bot", context -> Signal.stop())));

        CommandManager.register(CommandManager.literal("help").executes("帮助", context -> {
            Map<String, String> helps = CommandManager.getHelp(context.getSource());
            if (context.getSource() instanceof ConsoleCommandSource) {
                helps.forEach((key, value) -> context.getSource().sendFeedback(key + "\t" + value));
            } else {
                List<String> list = helps.entrySet().stream().parallel().map(it -> it.getKey() + "\t" + it.getValue()).toList();
                String commands = String.join("\n\n", list);
                context.getSource().sendFeedback(commands);
            }
        }));

    }
}
