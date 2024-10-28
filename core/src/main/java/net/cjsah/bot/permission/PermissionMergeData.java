package net.cjsah.bot.permission;

import net.cjsah.bot.exception.BuiltExceptions;

import java.util.ArrayList;
import java.util.List;

public class PermissionMergeData {
    private final List<String> rooms = new ArrayList<>();
    private PermissionType roomType = PermissionType.NONE;
    private final List<String> channels = new ArrayList<>();
    private PermissionType channelType = PermissionType.NONE;
    private final List<Long> users = new ArrayList<>();
    private PermissionType userType = PermissionType.NONE;
    private PermissionRole role = PermissionRole.USER;

    public void appendRooms(PermissionType type, List<String> rooms) {
        if (this.roomType != PermissionType.NONE && type != this.roomType) {
            throw BuiltExceptions.CONFLICT_MERGE_PERMISSION.create("房间", this.roomType, type);
        }
        this.rooms.addAll(rooms);
        this.roomType = type;
    }

    public void appendChannels(PermissionType type, List<String> channels) {
        if (this.channelType != PermissionType.NONE && type != this.channelType) {
            throw BuiltExceptions.CONFLICT_MERGE_PERMISSION.create("频道", this.channelType, type);
        }
        this.channels.addAll(channels);
        this.channelType = type;
    }

    public void appendUsers(PermissionType type, List<Long> users) {
        if (this.userType != PermissionType.NONE && type != this.userType) {
            throw BuiltExceptions.CONFLICT_MERGE_PERMISSION.create("房间", this.userType, type);
        }
        this.users.addAll(users);
        this.userType = type;
    }

    public void appendRole(PermissionRole role) {
        if (role == PermissionRole.DENY || role.getLevel() > this.role.getLevel()) {
            this.role = role;
        }
    }



    public enum PermissionType {
        NONE,
        WHITE,
        BLACK
    }

}
