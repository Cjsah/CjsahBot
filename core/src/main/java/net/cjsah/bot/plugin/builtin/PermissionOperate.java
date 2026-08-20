package net.cjsah.bot.plugin.builtin;

import net.cjsah.bot.command.argument.StringArgument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.registry.PluginCommandRegistry;

import java.util.Optional;

public class PermissionOperate {
    protected static LiteralArgumentBuilder register(PluginCommandRegistry registry) {
        // permission info
        // permission reload
        // permission global set user <user> role user/helper/admin
        // permission global set user <user> enabled/disabled
        // permission global set group <group> enabled/disabled
        // permission global set group_user <group> <user> role user/helper/admin
        // permission global set group_user <group> <user> enabled/disabled
        // permission global remove
        // permission plugin set
        // permission plugin remove



        return registry.literal("system")
            .then(registry.literal("permission")
                .executes(PermissionOperate::info)
                .then(registry.literal("info")
                    .executes(PermissionOperate::info)
                )
                .then(registry.literal("reload")
                    .executes(PermissionOperate::reload)
                )
            );

    }

    private static int reload(CommandContext context) {
        PermissionManager.getInstance().reload();
        context.getSource().sendFeedback("权限配置已重载");
        return 1;
    }

    private static int info(CommandContext context) {

        Optional<String> pluginId = StringArgument.get(context, "plugin");

        context.getSource().sendFeedback("权限配置如下:");
        return 1;
    }

    private static int globalSet(CommandContext context) {
        return 0;
    }

    private static int globalRemove(CommandContext context) {
        return 0;
    }

    private static int pluginSet(CommandContext context) {
        return 0;
    }

    public static int pluginRemove(CommandContext context) {
        return 0;
    }
}
