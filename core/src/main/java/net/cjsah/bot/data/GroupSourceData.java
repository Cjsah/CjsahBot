package net.cjsah.bot.data;

public class GroupSourceData {
    private final GroupData group;
    private final GroupUserData user;

    public GroupSourceData(GroupData group, GroupUserData user) {
        this.group = group;
        this.user = user;
    }

    public GroupData getGroup() {
        return this.group;
    }

    public GroupUserData getUser() {
        return this.user;
    }
}
