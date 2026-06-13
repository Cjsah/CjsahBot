package net.cjsah.bot.plugin.entry;

import net.cjsah.bot.exception.PluginAdapterException;
import net.cjsah.bot.loader.PluginClassLoader;
import net.cjsah.bot.plugin.Plugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class LoaderEntryPointImpl implements PluginEntryPoint {
    private final String value;
    private final ClassLoader loader;
    private Plugin instance = null;

    public LoaderEntryPointImpl(String value, ClassLoader loader) {
        this.value = value;
        this.loader = loader;
    }

    @Override
    public synchronized Plugin getOrCreate() {
        if (this.instance == null) {
            try {
                this.instance = createPlugin(this.value, this.loader);
            } catch (PluginAdapterException e) {
                PluginClassLoader.log.error("Fail to create plugin instance: {}", e.getMessage(), e);
            }
        }

        return this.instance;
    }

    private static Plugin createPlugin(String value, ClassLoader loader) throws PluginAdapterException {
        String[] methodSplit = value.split("::");

        if (methodSplit.length >= 3) {

            throw new PluginAdapterException("Invalid handle format: " + value);
        }

        Class<?> c;

        try {
            c = Class.forName(methodSplit[0], true, loader);
        } catch (ClassNotFoundException e) {
            throw new PluginAdapterException(e);
        }

        if (methodSplit.length == 1) {
            if (Plugin.class.isAssignableFrom(c)) {
                try {
                    return (Plugin) c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new PluginAdapterException(e);
                }
            } else {
                throw new PluginAdapterException("Class " + c.getName() + " cannot be cast to " + Plugin.class.getName() + "!");
            }
        } else /* length == 2 */ {
            List<Method> methodList = new ArrayList<>();

            for (Method m : c.getDeclaredMethods()) {
                if (!(m.getName().equals(methodSplit[1]))) {
                    continue;
                }

                methodList.add(m);
            }

            try {
                Field field = c.getDeclaredField(methodSplit[1]);
                Class<?> fType = field.getType();

                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    throw new PluginAdapterException("Field " + value + " must be static!");
                }

                if (!methodList.isEmpty()) {
                    throw new PluginAdapterException("Ambiguous " + value + " - refers to both field and method!");
                }

                if (!Plugin.class.isAssignableFrom(fType)) {
                    throw new PluginAdapterException("Field " + value + " cannot be cast to " + Plugin.class.getName() + "!");
                }

                return (Plugin) field.get(null);
            } catch (NoSuchFieldException e) {
                // ignore
            } catch (IllegalAccessException e) {
                throw new PluginAdapterException("Field " + value + " cannot be accessed!", e);
            }

            if (methodList.isEmpty()) {
                throw new PluginAdapterException("Could not find " + value + "!");
            } else if (methodList.size() >= 2) {
                throw new PluginAdapterException("Found multiple method entries of name " + value + "!");
            }

            final Method targetMethod = methodList.get(0);
            Object object = null;

            if ((targetMethod.getModifiers() & Modifier.STATIC) == 0) {
                try {
                    object = c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new PluginAdapterException(e);
                }
            }

            MethodHandle handle;

            try {
                handle = MethodHandles.lookup()
                    .unreflect(targetMethod);
            } catch (Exception ex) {
                throw new PluginAdapterException(ex);
            }

            if (object != null) {
                handle = handle.bindTo(object);
            }

            // uses proxy as well, but this handles default and object methods
            try {
                return MethodHandleProxies.asInterfaceInstance(Plugin.class, handle);
            } catch (Exception ex) {
                throw new PluginAdapterException(ex);
            }
        }
    }

}
