package net.cjsah.bot.data;

import lombok.Data;

@Data
public class GroupData {
    private final long groupId;
    private final String groupName;
    private final int memberCount;
    private final int maxMemberCount;
}
