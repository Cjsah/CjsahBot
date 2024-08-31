package net.cjsah.bot.permission;

public class PermissionRoleNode {
    private PermissionStatus groupStatus;
    private PermissionStatus userStatus;
    private RoleType role;

    public PermissionRoleNode() {
        this.groupStatus = PermissionStatus.PRE_DENY;
        this.userStatus = PermissionStatus.PRE_DENY;
        this.role = RoleType.DENY;
    }
}
