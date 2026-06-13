package net.cjsah.bot.command;

import lombok.Setter;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;

@SuppressWarnings("unused")
public class StringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    @Setter
    private int cursor;

    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(final String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public int getRemainingLength() {
        return string.length() - cursor;
    }

    public int getTotalLength() {
        return string.length();
    }

    public int getCursor() {
        return cursor;
    }

    public String getRead() {
        return string.substring(0, cursor);
    }

    public String getRemaining() {
        return string.substring(cursor);
    }

    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    public boolean canRead() {
        return canRead(1);
    }

    public char peek() {
        return string.charAt(cursor);
    }

    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public byte readByte() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_BYTE.create();
        }
        try {
            return Byte.parseByte(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_BYTE.create(number);
        }
    }

    public short readShort() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_SHORT.create();
        }
        try {
            return Short.parseShort(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_SHORT.create(number);
        }
    }

    public int readInt() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_INT.create();
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_INT.create(number);
        }
    }

    public long readLong() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_LONG.create();
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_LONG.create(number);
        }
    }

    public double readDouble() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_DOUBLE.create();
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_DOUBLE.create(number);
        }
    }

    public float readFloat() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw BuiltinExceptions.READER_EXPECTED_FLOAT.create();
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw BuiltinExceptions.READER_INVALID_FLOAT.create(number);
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_' || c == '-'
            || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws CommandException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw BuiltinExceptions.READER_EXPECTED_START_OF_QUOTE.create();
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw BuiltinExceptions.READER_INVALID_ESCAPE.create(c);
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw BuiltinExceptions.READER_EXPECTED_END_OF_QUOTE.create();
    }

    public String readString() throws CommandException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws CommandException {
        final int start = cursor;
        final String value = readString();
        return switch (value) {
            case "" -> throw BuiltinExceptions.READER_EXPECTED_BOOL.create();
            case "true" -> true;
            case "false" -> false;
            default -> {
                cursor = start;
                throw BuiltinExceptions.READER_INVALID_BOOL.create(value);
            }
        };
    }

    public void expect(final char c) throws CommandException {
        if (!canRead() || peek() != c) {
            throw BuiltinExceptions.READER_EXPECTED_SYMBOL.create(c);
        }
        skip();
    }

}
