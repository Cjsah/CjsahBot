package net.cjsah.bot.loader;

import java.io.Closeable;

public interface CloseableClassLoader extends Closeable {
    void close();
}
