package net.cjsah.bot.plugin;

import java.util.Collections;

public class MainPlugin extends Plugin {
    public static final Plugin INSTANCE = new MainPlugin();
    public static final PluginInfo PLUGIN_INFO = new PluginInfo(
            "main",
            "Main",
            "Main",
            "1.0",
            Collections.singletonMap("authors", Collections.singletonList("Cjsah"))
    );
}
