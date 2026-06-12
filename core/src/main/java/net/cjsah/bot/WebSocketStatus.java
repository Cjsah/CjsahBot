package net.cjsah.bot;

public enum WebSocketStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ;

    public boolean isConnected() {
        return this == CONNECTED;
    }
}
