package net.cjsah.bot.util;

import org.jetbrains.annotations.Nullable;

public final class EnumUtil {
    @Nullable
    public static <T extends Enum<T>> T ofName(Class<T> clazz, String name) {
        return ofName(clazz, name, null);
    }

    @Nullable
    public static <T extends Enum<T>> T ofName(Class<T> clazz, String name, @Nullable T defaultValue) {
        for (T t : clazz.getEnumConstants()) {
            if (t.name().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return defaultValue;
    }
}
