package net.cjsah.bot.permission;

public class PermissionRoleNode {
    private boolean allow = false;
    private boolean deny = false;
    private RoleType role = null;

    public PermissionRoleNode() {}

    public void allow() {
        this.allow = true;
    }

    public void deny() {
        this.deny = true;
    }

    public boolean isDeny() {
        return this.deny || this.role == RoleType.DENY;
    }

    public RoleType getRole() {
        if (this.allow) return this.role == null ? RoleType.USER : this.role;
        return this.role == null ? RoleType.ALL : this.role;
    }

    public void setRole(RoleType role) {
        if (this.role == null || role.getLevel() > this.role.getLevel()) {
            this.role = role;
        }
    }
}
