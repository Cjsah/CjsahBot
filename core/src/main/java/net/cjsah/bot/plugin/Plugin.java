package net.cjsah.bot.plugin;

public interface Plugin {
    void load();

    default void unload() {}
}
