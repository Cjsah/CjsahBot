package net.cjsah.bot.plugin;

import lombok.Getter;
import net.cjsah.bot.exception.PluginAdapterException;
import net.cjsah.bot.loader.PluginClassLoader;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PluginEntrypoint {
    private final String value;
    private final ClassLoader loader;
    private final Map<Class<?>, Object> instances;

    public PluginEntrypoint(String value, ClassLoader loader) {
        this.value = value;
        this.loader = loader;
        this.instances = new IdentityHashMap<>(1);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public synchronized <T> T getOrCreate(Class<T> type) {
        T ret = (T) this.instances.get(type);

        if (ret == null) {
            try {
                ret = createPlugin(this.value, type, this.loader);
                T prev = (T) this.instances.putIfAbsent(type, ret);
                if (prev != null) ret = prev;
            } catch (PluginAdapterException e) {
                PluginClassLoader.log.error("Fail to create plugin instance: {}", e.getMessage(), e);
            }
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private static <T> T createPlugin(String value, Class<T> type, ClassLoader loader) throws PluginAdapterException {
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
            if (type.isAssignableFrom(c)) {
                try {
                    return (T) c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new PluginAdapterException(e);
                }
            } else {
                throw new PluginAdapterException("Class " + c.getName() + " cannot be cast to " + type.getName() + "!");
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

                if (!type.isAssignableFrom(fType)) {
                    throw new PluginAdapterException("Field " + value + " cannot be cast to " + type.getName() + "!");
                }

                return (T) field.get(null);
            } catch (NoSuchFieldException e) {
                // ignore
            } catch (IllegalAccessException e) {
                throw new PluginAdapterException("Field " + value + " cannot be accessed!", e);
            }

            if (!type.isInterface()) {
                throw new PluginAdapterException("Cannot proxy method " + value + " to non-interface type " + type.getName() + "!");
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
                return MethodHandleProxies.asInterfaceInstance(type, handle);
            } catch (Exception ex) {
                throw new PluginAdapterException(ex);
            }
        }
    }

}
