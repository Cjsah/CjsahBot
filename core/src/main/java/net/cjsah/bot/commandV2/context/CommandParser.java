package net.cjsah.bot.commandV2.context;

import net.cjsah.bot.commandV2.argument.ArgsArgument;
import net.cjsah.bot.commandV2.argument.Argument;
import net.cjsah.bot.exception.BuiltExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public final class CommandParser {
    private final String cmd;
    private int cursor;

    public CommandParser(String cmd) {
        this.cmd = cmd;
        this.cursor = 1;
        if (cmd.codePointAt(0) != '/') {
            throw BuiltExceptions.COMMAND_PATTERN_ERROR.create(cmd);
        }
    }

    public CommandNode parse(List<? extends Class<?>> parameterTypes) {
        Pattern pattern = Pattern.compile("^<[a-zA-Z_]\\w*>$");
        String root = this.readUnquotedString();
        this.skipWhitespace();
        List<String> params = new ArrayList<>();
        while (this.canRead()) {
            String node = this.readUnquotedString();
            if (!pattern.matcher(node).matches()) {
                throw BuiltExceptions.ERROR_PARSE_PARAM_NAME.create(node);
            }
            params.add(node.substring(1, node.length() - 1));
            this.skipWhitespace();
        }
        CommandNode node = new CommandNode(root);
        for (int index = 0; index < parameterTypes.size(); index++) {
            Class<?> type = parameterTypes.get(index);
            Argument<?> generator = Argument.generate(type);
            if (generator instanceof ArgsArgument) {
                node.appendParameter(new CommandParameter("", generator));
                continue;
            }
            if (params.isEmpty()) {
                throw BuiltExceptions.ERROR_PARAM_MISSING.create(index);
            }
            String name = params.removeFirst();
            node.appendParameter(new CommandParameter(name, generator));
        }
        return node;

    }

    public boolean canRead() {
        return this.cursor + 1 <= cmd.length();
    }

    public char peek() {
        return this.cmd.charAt(this.cursor);
    }

    public void skip() {
        this.cursor++;
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || c == '<' || c == '>';
    }

    public void skipWhitespace() {
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
    }

    public String readUnquotedString() {
        int start = this.cursor;
        while (this.canRead() && isAllowedInUnquotedString(this.peek())) { this.skip(); }
        return this.cmd.substring(start, this.cursor);
    }
}
