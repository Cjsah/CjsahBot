package net.cjsah.bot.plugin;

import lombok.Getter;
import net.cjsah.bot.util.PluginUtil;

import java.util.IdentityHashMap;
import java.util.Map;

@Getter
public class PluginEntrypoint {
    private final String value;
    private final Map<Class<?>, Object> instances;

    public PluginEntrypoint(String value) {
        this.value = value;
        this.instances = new IdentityHashMap<>(1);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T getOrCreate(Class<T> type, ClassLoader loader) {
        T ret = (T) this.instances.get(type);

        if (ret == null) {
            ret = PluginUtil.createPlugin(this.value, type, loader);
            T prev = (T) this.instances.putIfAbsent(type, ret);
            if (prev != null) ret = prev;
        }

        return ret;
    }
}
