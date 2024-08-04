package net.cjsah.bot.command;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;


public class StringReader {
    private final String string;
    private int cursor = 0;
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    public StringReader(String string) {
        this.string = string;
    }

    public StringReader copy() {
        StringReader reader = new StringReader(string);
        reader.cursor = this.cursor;
        return reader;
    }

    public String getString() {
        return this.string;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return this.cursor;
    }

    public int getTotalLength() {
        return this.string.length();
    }

    public int getRemainLength() {
        return this.string.length() - this.cursor;
    }

    public String getRead() {
        return this.string.substring(0, this.cursor);
    }

    public String getRemaining() {
        return this.string.substring(this.cursor);
    }

    public boolean canRead(int length) {
        return this.cursor + length <= string.length();
    }

    public boolean canRead() {
        return this.canRead(1);
    }

    public char peek() {
        return this.string.charAt(this.cursor);
    }

    public char peek(int offset) {
        return this.string.charAt(this.cursor + offset);
    }

    public char read() {
        return this.string.charAt(this.cursor++);
    }

    public void skip() {
        this.cursor++;
    }

    public static boolean isAllowedNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.' || c == '-';
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
    }

    public int readInt() throws CommandException {
        int start = this.cursor;
        while (this.canRead() && isAllowedNumber(this.peek())) { this.skip(); }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_INT.create();
        }
        try {
            return Integer.parseInt(number);
        }catch (NumberFormatException e) {
            this.cursor = start;
            throw BuiltExceptions.READER_INVALID_INT.create(number);
        }
    }

    public long readLong() throws CommandException {
        int start = this.cursor;
        while (this.canRead() && isAllowedNumber(this.peek())) { this.skip(); }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_LONG.create();
        }
        try {
            return Long.parseLong(number);
        }catch (NumberFormatException e) {
            this.cursor = start;
            throw BuiltExceptions.READER_INVALID_LONG.create(number);
        }
    }

    public double readDouble() throws CommandException {
        int start = this.cursor;
        while (this.canRead() && isAllowedNumber(this.peek())) { this.skip(); }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_DOUBLE.create();
        }
        try {
            return Double.parseDouble(number);
        }catch (NumberFormatException e) {
            this.cursor = start;
            throw BuiltExceptions.READER_INVALID_DOUBLE.create(number);
        }
    }

    public float readFloat() throws CommandException {
        int start = this.cursor;
        while (this.canRead() && isAllowedNumber(this.peek())) { this.skip(); }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_FLOAT.create();
        }
        try {
            return Float.parseFloat(number);
        }catch (NumberFormatException e) {
            this.cursor = start;
            throw BuiltExceptions.READER_INVALID_FLOAT.create(number);
        }
    }

    public String readUnquotedString() {
        int start = this.cursor;
        while (this.canRead() && isAllowedInUnquotedString(this.peek())) { this.skip(); }
        return this.string.substring(start, this.cursor);
    }

    public String readQuotedString() throws CommandException {
        if (!this.canRead()) return "";
        char next = this.peek();
        if (!isQuotedStringStart(next)) {
            throw BuiltExceptions.READER_EXPECTED_START_OF_QUOTE.create();
        }
        this.skip();
        return this.readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandException {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (this.canRead()) {
            char c = this.read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    this.cursor--;
                    throw BuiltExceptions.READER_INVALID_ESCAPE.create(c);
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }
        throw BuiltExceptions.READER_EXPECTED_END_OF_QUOTE.create();
    }

    public String readString() throws CommandException {
        if (!this.canRead()) return "";
        char next = this.peek();
        if (!isQuotedStringStart(next)) {
            this.skip();
            return this.readStringUntil(next);
        }
        return this.readUnquotedString();

    }

    public boolean readBoolean() throws CommandException {
        int start = this.cursor;
        String value = this.readString();
        if (value.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_BOOL.create();
        }
        return switch (value) {
            case "true" -> true;
            case "false" -> false;
            default -> {
                this.cursor = start;
                throw BuiltExceptions.READER_INVALID_BOOL.create(value);
            }
        };
    }

    public void expect(char c) throws CommandException {
        if (!this.canRead() || this.peek() != c) {
            throw BuiltExceptions.READER_EXPECTED_SYMBOL.create(c);
        }
        this.skip();
    }
}
