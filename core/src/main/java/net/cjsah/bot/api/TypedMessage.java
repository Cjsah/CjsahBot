package net.cjsah.bot.api;

public class TypedMessage<T> {
    private final int type;
    private final String key;
    private final T content;

    private TypedMessage(int type, String key, T content) {
        this.type = type;
        this.key = key;
        this.content = content;
    }

    public static TypedMessage<String> text(String content) {
        return new TypedMessage<>(0, "content", content);
    }

    public int getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public T getContent() {
        return this.content;
    }
}
