package net.cjsah.bot.permission;

public class PermissionRoleNode {
    private boolean allow = false;
    private boolean deny = false;
    private PermissionRole role = null;

    public PermissionRoleNode() {}

    public void allow() {
        this.allow = true;
    }

    public void deny() {
        this.deny = true;
    }

    public boolean isDeny() {
        return this.deny || this.role == PermissionRole.DENY;
    }

    public PermissionRole getRole() {
        if (this.allow) return this.role == null ? PermissionRole.USER : this.role;
        return this.role == null ? PermissionRole.ALL : this.role;
    }

    public void setRole(PermissionRole role) {
        if (this.role == null || role.getLevel() > this.role.getLevel()) {
            this.role = role;
        }
    }
}
