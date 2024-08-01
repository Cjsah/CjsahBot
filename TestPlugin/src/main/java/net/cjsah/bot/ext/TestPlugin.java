package net.cjsah.bot.ext;

import net.cjsah.bot.plugin.Plugin;

public class TestPlugin extends Plugin {
    @Override
    public void onLoad() {
        System.out.println("load");
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
