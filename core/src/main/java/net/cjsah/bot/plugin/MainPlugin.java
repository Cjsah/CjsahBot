package net.cjsah.bot.plugin;

import net.cjsah.bot.Signal;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.ConsoleCommandSource;
import net.cjsah.bot.permission.RoleType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainPlugin extends Plugin {
    public static MainPlugin INSTANCE = new MainPlugin();
    public static PluginInfo PLUGIN_INFO = new PluginInfo("main", "Main", "Main", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {

        CommandManager.register(CommandManager.literal("console").requires(it -> it.hasPermission(RoleType.ADMIN)).then(CommandManager.literal("stop").executes("关闭Bot", context -> Signal.stop())));

        CommandManager.register(CommandManager.literal("help").executes("帮助", context -> {

            Map<String, String> helps = CommandManager.getHelp(context.getSource());
            if (context.getSource() instanceof ConsoleCommandSource) {
                helps.forEach((key, value) -> context.getSource().sendFeedback(key + "\t" + value));
            } else {
                List<String> list = helps.entrySet().stream().parallel().map(it -> it.getKey() + "\t" + it.getValue()).toList();
                String commands = String.join("\n", list);
                context.getSource().sendFeedback(commands);
            }
        }));

    }
}
