package net.cjsah.bot.commandV2.context;

import net.cjsah.bot.commandV2.CommandParam;
import net.cjsah.bot.commandV2.argument.Argument;
import net.cjsah.bot.commandV2.argument.special.EmptyArgument;
import net.cjsah.bot.commandV2.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
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

    public CommandNodeBuilder parse(Parameter[] parameters) {
        Pattern pattern = Pattern.compile("^<\\w*>$");
        String root = this.readNode();
        CommandNodeBuilder builder = new CommandNodeBuilder(root);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            boolean isSpecial = Map.class.isAssignableFrom(type);
            if (isSpecial) verifyMapValidation(parameter);
            if (CommandSource.class.isAssignableFrom(type)) {
                isSpecial = true;
            }
            CommandParam annotation = parameter.getAnnotation(CommandParam.class);
            String name = annotation == null ? "" : annotation.value();
            if (!isSpecial && this.canRead()) {
                String str = this.readNode();
                if (!pattern.matcher(str).matches()) {
                    throw BuiltExceptions.ERROR_PARSE_PARAM_NAME.create(str);
                }
                if (name.isEmpty()) {
                    name = str.substring(1, str.length() - 1);
                }
            }
            if (!isSpecial && name.isEmpty()) {
                throw BuiltExceptions.NO_PARAM_NAME.create(i + 1);
            }
            Class<? extends Argument<?>> resolver = annotation == null ? EmptyArgument.class : annotation.resolver();
            if (resolver != EmptyArgument.class) {
                builder.appendParameter(new CommandParameter(name, annotation.description(), resolver));
                continue;
            }
            String description = annotation == null ? "" : annotation.description();
            builder.appendParameter(new CommandParameter(name, description, Argument.getResolver(type)));
        }
        return builder;
    }

    private static void verifyMapValidation(Parameter parameter) {
        Type type = parameter.getParameterizedType();
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            if (actualTypes.length == 2 && "java.lang.String".equals(actualTypes[0].getTypeName()) && "java.lang.String".equals(actualTypes[1].getTypeName())) {
                return;
            }
        }
        throw BuiltExceptions.EXPECTED_STRING_MAP.create();
    }

    public boolean canRead() {
        return this.cursor + 1 <= this.cmd.length();
    }

    public char peek() {
        return this.cmd.charAt(this.cursor);
    }

    public void skip() {
        this.cursor++;
    }

    public static boolean isAllowedInNode(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || c == '<' || c == '>';
    }

    public String readNode() {
        int start = this.cursor;
        while (this.canRead() && isAllowedInNode(this.peek())) { this.skip(); }
        String value = this.cmd.substring(start, this.cursor);
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
        return value;
    }
}
