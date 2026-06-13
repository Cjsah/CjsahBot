package net.cjsah.bot.loader;

import java.net.URL;
import java.util.Enumeration;

public class DummyClassLoader extends ClassLoader implements CloseableClassLoader {
    private static final Enumeration<URL> NULL_ENUMERATION = new Enumeration<>() {
        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public URL nextElement() {
            return null;
        }
    };

    static {
        registerAsParallelCapable();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    @Override
    public URL getResource(String name) {
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String var1) {
        return NULL_ENUMERATION;
    }

    @Override
    public void close() {
    }
}
