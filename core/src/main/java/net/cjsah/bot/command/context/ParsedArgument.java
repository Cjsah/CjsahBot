package net.cjsah.bot.command.context;

import java.util.Objects;

public class ParsedArgument<T> {
    private final StringRange range;
    private final T result;

    public ParsedArgument(final int start, final int end, final T result) {
        this.range = StringRange.between(start, end);
        this.result = result;
    }

    public StringRange getRange() {
        return this.range;
    }

    public T getResult() {
        return this.result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ParsedArgument<?> that)) return false;

        return Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.range, this.result);
    }
}
