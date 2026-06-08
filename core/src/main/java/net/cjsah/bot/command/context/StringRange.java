package net.cjsah.bot.command.context;

import net.cjsah.bot.command.StringReader;

public record StringRange(int start, int end) {

    public static StringRange at(final int pos) {
        return new StringRange(pos, pos);
    }

    public static StringRange between(final int start, final int end) {
        return new StringRange(start, end);
    }

    public static StringRange encompassing(final StringRange a, final StringRange b) {
        return new StringRange(Math.min(a.start(), b.start()), Math.max(a.end(), b.end()));
    }

    public String get(final StringReader reader) {
        return reader.getString().substring(this.start, this.end);
    }

    public String get(final String string) {
        return string.substring(this.start, this.end);
    }

    public boolean isEmpty() {
        return this.start == this.end;
    }

    public int getLength() {
        return this.end - this.start;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StringRange(int start1, int end1))) return false;
        return this.start == start1 && this.end == end1;
    }

    @Override
    public String toString() {
        return "StringRange{" +
            "start=" + this.start +
            ", end=" + this.end +
            '}';
    }

}
