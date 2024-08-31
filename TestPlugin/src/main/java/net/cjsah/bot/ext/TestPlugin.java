package net.cjsah.bot.ext;

import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.GroupCommandSource;
import net.cjsah.bot.data.GroupSourceData;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.RoleType;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.plugin.PluginContext;

public class TestPlugin extends Plugin {
    @Override
    public void onLoad() {
        CommandManager.register(CommandManager.literal("test").executes("插件测试", context -> {
            CommandSource<?> source = context.getSource();
            if (source instanceof GroupCommandSource groupSource) {
                GroupSourceData sender = groupSource.getSender();

                boolean b = PermissionManager.hasPermission(PluginContext.getCurrentPluginInfo().getId(), sender.group().getGroupId(), sender.user().getUserId(), RoleType.ADMIN);
                source.sendFeedback(String.format("你%s管理员权限", b ? "拥有" : "没有"));

            }


        }));

    }

    @Override
    public void onStarted() {
        System.out.println("started");
    }

    @Override
    public void onUnload() {
        System.out.println("unload");
    }
}
