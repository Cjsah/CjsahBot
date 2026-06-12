package net.cjsah.bot.data;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IStrSerializable {
    String getSerializedName();

    static <E extends Enum<E> & IStrSerializable> Codec<E> fromEnum(Class<E> clazz) {
        return fromEnum(clazz, IStrSerializable::getSerializedName);
    }

    static <E extends Enum<E>> Codec<E> fromEnum(Class<E> clazz, Function<E, String> mapper) {
        E[] enums = clazz.getEnumConstants();
        Function<String, E> function = createNameLookup(enums, mapper);
        return Codec.stringResolver(mapper, function);
    }

    static <T> Function<String, T> createNameLookup(T[] objects, Function<T, String> function) {
        if (objects.length > 16) {
            Map<String, T> map = Arrays.stream(objects).collect(Collectors.toMap(function, object -> object));
            return map::get;
        } else {
            return string -> {
                for (T object : objects) {
                    if (function.apply(object).equals(string)) {
                        return object;
                    }
                }
                return null;
            };
        }
    }
}
