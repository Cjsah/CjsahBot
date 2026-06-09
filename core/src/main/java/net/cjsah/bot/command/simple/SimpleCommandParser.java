package net.cjsah.bot.command.simple;

import net.cjsah.bot.command.CommandRegisterContext;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.argument.ArgumentManager;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.execute.MethodCommand;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleCommandParser {
    private static final Pattern PATTERN_ARGUMENT = Pattern.compile("^<(\\w+):(\\w+)(?::(\\w+))?>$");
    private static final Pattern PATTERN_LITERAL = Pattern.compile("^\\w+$");
    private final CommandRegisterContext context;
    private final String cmd;
    private int cursor;

    public SimpleCommandParser(CommandRegisterContext context, String cmd) {
        this.context = context;
        this.cmd = cmd;
        this.cursor = 1;
    }

    public LiteralArgumentBuilder parse(Method method) throws CommandException {
        if (!this.canRead()) {
            throw BuiltExceptions.PARSE_EMPTY_STRING.create();
        }
        ArgumentBuilder<?> root = nextBuilder();
        if (!(root instanceof LiteralArgumentBuilder)) {
            throw BuiltExceptions.PARSE_ROOT_ARGUMENT.create();
        }
        Map<String, Class<?>> args = new HashMap<>();
        ArgumentBuilder<?> last = root;
        while (this.canRead()) {
            ArgumentBuilder<?> next = nextBuilder();
            last.then(next);
            last = next;

            if (next instanceof RequiredArgumentBuilder<?> builder) {
                try {
                    Method parseMethod = builder.getClass().getDeclaredMethod("parse", StringReader.class);
                    args.put(builder.getName(), parseMethod.getReturnType());
                } catch (NoSuchMethodException ignored) {
                }
            }
        }

        Parameter[] parameters = method.getParameters();

        List<ParamInfo> types = Arrays.stream(parameters)
            .map(parameter -> {
                CommandParam annotation = parameter.getAnnotation(CommandParam.class);
                String name = annotation != null && !annotation.value().isEmpty() ? annotation.value() : parameter.getName();
                Class<?> type = parameter.getType();
                if (CommandContext.class.isAssignableFrom(type)) {
                    return new ParamInfo(type, ParamInfo.self());
                }
                Class<?> clazz = args.get(name);
                if (type.isAssignableFrom(clazz)) {
                    return new ParamInfo(type, ParamInfo.arg(name, clazz));
                }
                if (CommandSource.class.isAssignableFrom(clazz)) {
                    return new ParamInfo(type, ParamInfo.source());
                }
                return new ParamInfo(type, ParamInfo.empty());
            })
            .toList();

        last.executes(new MethodCommand(method, types));
        return (LiteralArgumentBuilder) root;
    }

    private ArgumentBuilder<?> nextBuilder() throws CommandException {
        String node = this.next();
        Matcher argumentMatcher = PATTERN_ARGUMENT.matcher(node);
        if (argumentMatcher.matches()) {
            String name = argumentMatcher.group(1);
            String type = argumentMatcher.group(2);
            String extra = argumentMatcher.group(3);
            Argument<?> argument = ArgumentManager.getArgument(type, extra);
            return this.context.argument(name, argument);
        }
        Matcher literalMatcher = PATTERN_LITERAL.matcher(node);
        if (!literalMatcher.matches()) {
            throw BuiltExceptions.PARSE_INVALID_NODE.create(node);
        }
        return this.context.literal(node);
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
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || c == '<' || c == '>' || c == ':';
    }

    public String next() {
        int start = this.cursor;
        while (this.canRead() && isAllowedInNode(this.peek())) {
            this.skip();
        }
        String value = this.cmd.substring(start, this.cursor);
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            this.skip();
        }
        return value;
    }
}
