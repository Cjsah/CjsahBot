package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.UserRole;

import java.util.Collection;

public class SingleGroupPermissionContext extends PermissionContext {
    private final Permissions permissions;
    private final long groupId;
    private final Enabled enabled;

    public SingleGroupPermissionContext(Permissions permissions, long groupId) {
        this.permissions = permissions;
        this.groupId = groupId;
        this.enabled = permissions.global()
            .getGroup(groupId)
            .map(it -> Enabled.from(it.enabled()))
            .orElse(Enabled.UNSET);
    }

    @Override
    public boolean hasPermission(UserRole role) {
        return this.enabled.enabled();
    }

    @Override
    public boolean hasPermission(UserRole role, Collection<String> pluginIds) {
        for (String pluginId : pluginIds) {
            PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
            Enabled enabled = this.enabled;
            if (plugin != null) {

                enabled = plugin.getGroup(this.groupId)
                    .map(it -> Enabled.from(it.enabled()))
                    .orElse(enabled);

                if (enabled == Enabled.UNSET) {
                    enabled = Enabled.from(plugin.defaultEnabled());
                }
            }
            if (enabled.enabled()) {
                return true;
            }
        }
        return pluginIds.isEmpty() && this.hasPermission(role);
    }

    @Override
    public boolean hasPermission(String pluginId) {
        PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
        Enabled enabled = this.enabled;
        if (plugin != null) {

            enabled = plugin.getGroup(this.groupId)
                .map(it -> Enabled.from(it.enabled()))
                .orElse(enabled);

            if (enabled == Enabled.UNSET) {
                enabled = Enabled.from(plugin.defaultEnabled());
            }
        }
        return enabled.enabled();
    }
}
