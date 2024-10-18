package net.cjsah.bot.permission;

public enum Permission {
    ADMIN,
    CHANNEL_VIEW,
    CHANNEL_MANAGE,
    LOG_VIEW,
    ROLE_MANAGE,
    ROOM_MANAGE,
    INVITE_CREATE,
    INVITE_MANAGE,
    NICKNAME_MODIFY,
    NICKNAME_MANAGE,
    KICK_USER_FROM_ROOM,
    KICK_USER_FROM_CHANNEL,
    APPEND_BLACKLIST,
    MUTE_USER,
    SEND_MESSAGE,
    UPLOAD_FILE,
    AT_ALL,
    EMOJI_REPLY,
    MESSAGE_MANAGE,
    RECEIVE_OFFLINE_MSG,
    INVITATION_PLAY_CREATE,
    INVITATION_PLAY_MANAGE,
    JOIN_VOICE_CHANNEL,
    CHAT_IN_CHANNEL,
    CHAT_IN_CHANNEL_DIRECTLY,
    MUTE_VOICE_CHANNEL,
    MUTE_USER_VOICE,
    MOVE_USER,
    STATEMENT_MANAGE,
    INVITATION_OTHER_MANAGE,
    EMOJI_MANAGE,
    VOICE_PACK_MANAGE,
    VOICE_PACK_PLAY,
    ACCOMPANIMENT_PLAY,
    SHARE_SCREEN,
    GROUP_PUBLISH,
    USE_BOT_COMMAND,
    ;

    private final long value;

    Permission() {
        this.value = 1L << this.ordinal();
    }

    public long getValue() {
        return this.value;
    }
}
