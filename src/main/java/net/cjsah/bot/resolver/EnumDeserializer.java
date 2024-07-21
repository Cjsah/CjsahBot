package net.cjsah.bot.resolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.EnumUtil;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class EnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> implements ContextualDeserializer {
    private final Class<T> clazz;
    private final String name;
    private final String key;

    public EnumDeserializer() {
        this.clazz = null;
        this.name = null;
        this.key = null;
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        if (this.clazz == null || this.name == null || this.key == null) return null;
        JsonNode node = parser.getCodec().readTree(parser);
        String text = node.asText();
        T[] values = this.clazz.getEnumConstants();
        if (text == null) return values[0];
        return Arrays.stream(values)
                .filter(it -> EnumUtil.getFromKey(it, this.key).equals(text))
                .findFirst()
                .orElse(values[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
        String key = property.getAnnotation(JsonEnumMapping.class).value();
        Class<?> clazz = property.getType().getRawClass();
        return new EnumDeserializer<>((Class<T>) clazz, property.getName(), key);
    }
}
