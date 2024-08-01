package net.cjsah.bot.plugin;

public abstract class Plugin {
    protected static final ThreadLocal<Plugin> THREAD_LOCAL = new ThreadLocal<>();

}
