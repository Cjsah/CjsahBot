package net.cjsah.bot.plugin.entry;

import lombok.RequiredArgsConstructor;
import net.cjsah.bot.plugin.Plugin;

@RequiredArgsConstructor
public class BuiltinPluginEntryPointImpl implements PluginEntryPoint {
    private final Plugin instance;

    @Override
    public Plugin getOrCreate() {
        return this.instance;
    }
}
