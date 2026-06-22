package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.permission.UserRole;

import java.util.Collection;

public abstract class PermissionContext {

    public abstract boolean hasPermission(UserRole role);

    public abstract boolean hasPermission(UserRole role, Collection<String> pluginIds);

    public abstract boolean hasPermission(String pluginId);

    protected enum Enabled {
        UNSET(true),
        ENABLED(true),
        DISABLED(false);

        private final boolean enabled;

        Enabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean enabled() {
            return this.enabled;
        }

        public static Enabled from(boolean enabled) {
            return enabled ? ENABLED : DISABLED;
        }
    }
}
