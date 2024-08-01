package net.cjsah.bot.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginLoader extends URLClassLoader {
    public PluginLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
