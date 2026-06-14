package net.cjsah.bot;

public enum AppStatus {
    INIT,
    RUNNING,
    STOPPING,
    STOPPED,
    ;

    public boolean isRunning() {
        return this == RUNNING;
    }
}
