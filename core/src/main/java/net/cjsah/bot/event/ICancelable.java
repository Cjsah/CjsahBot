package net.cjsah.bot.event;

public interface ICancelable {
    boolean isCancel();
    void cancel();
}
