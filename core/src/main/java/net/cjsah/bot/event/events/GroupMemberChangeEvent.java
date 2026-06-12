package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.ChangeType;

@Getter
@RequiredArgsConstructor
public abstract class GroupMemberChangeEvent extends ReceivedEvent {
    protected final long groupId;
    protected final long userId;
    protected final long operatorId;
    protected final ChangeType type;
}
