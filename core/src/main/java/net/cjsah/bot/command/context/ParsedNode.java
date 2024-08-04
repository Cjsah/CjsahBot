package net.cjsah.bot.command.context;

public class ParsedNode<T> {
    private final int start;
    private final int end;
    private final T result;

    public ParsedNode(int start, int end, T result) {
        this.start = start;
        this.end = end;
        this.result = result;
    }

    public Range getRange() {
        return new Range(this.start, this.end);
    }

    public T getResult() {
        return this.result;
    }
}
