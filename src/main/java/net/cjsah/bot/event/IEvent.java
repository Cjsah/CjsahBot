package net.cjsah.bot.event;

public interface IEvent {
    default boolean cancelable() { return false; }
    default boolean isCancel() { return false; }
    default void cancel() {}
}
