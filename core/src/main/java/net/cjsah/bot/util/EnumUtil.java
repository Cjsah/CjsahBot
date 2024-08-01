package net.cjsah.bot.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class EnumUtil {
    @SneakyThrows
    public static <T extends Enum<T>> String getFromKey(T entry, String key) {
        Field field = entry.getDeclaringClass().getDeclaredField(key);
        field.setAccessible(true);
        String value = (String) field.get(entry);
        field.setAccessible(false);
        return value;
    }

}
