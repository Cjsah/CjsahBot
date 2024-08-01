package net.cjsah.bot.ext;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.plugin.Plugin;

public class TestPlugin extends Plugin {
    @Override
    public void onLoad() {
        EventManager.subscribe(MessageEvent.GroupMessageEvent.class, event -> {
            if (event.getGroupId() == 799652476L && "/api".equals(event.getRawMessage())) {
                Api.sendGroupMsg(event.getGroupId(), MessageChain.raw("插件测试"));
            }
        });
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
