package net.cjsah.bot.command.context;

public class Range {
    public final int start;
    public final int end;

    public Range(int pos) {
        this.start = pos;
        this.end = pos;
    }

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Range(Range a, Range b) {
        this(Math.min(a.start, b.start), Math.max(a.end, b.end));
    }

    public boolean isEmpty() {
        return this.start == this.end;
    }
}
