package net.cjsah.bot.permission.context;

import net.cjsah.bot.permission.PermissionRole;

import java.util.Collection;

public abstract class PermissionContext {

    public abstract boolean hasPermission(PermissionRole role);

    public abstract boolean hasPermission(PermissionRole role, Collection<String> pluginIds);

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
