package net.cjsah.bot;

public enum AppStatus {
    INIT,
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
