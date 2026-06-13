package net.cjsah.bot;

public enum AppStatus {
    PREPARED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    ;

    public boolean isRunning() {
        return this == STARTED;
    }
}
