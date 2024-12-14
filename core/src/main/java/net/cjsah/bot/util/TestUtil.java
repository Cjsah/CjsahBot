package net.cjsah.bot.util;

public class TestUtil {
    public static void printStackTrace() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        for (StackTraceElement trace : traces) {
            System.out.println(trace);
        }
    }
}
