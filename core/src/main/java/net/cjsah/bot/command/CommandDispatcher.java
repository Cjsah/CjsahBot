package net.cjsah.bot.command;

import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.LiteralCommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;

import java.util.*;
import java.util.function.Predicate;

public class CommandDispatcher {
    public static final String ARGUMENT_SEPARATOR = " ";

    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";

    private final RootCommandNode root;

    private final Predicate<CommandNode> hasCommand = new Predicate<>() {
        @Override
        public boolean test(final CommandNode input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(hasCommand));
        }
    };

    public CommandDispatcher(final RootCommandNode root) {
        this.root = root;
    }

    public CommandDispatcher() {
        this(new RootCommandNode());
    }

    public LiteralCommandNode register(final LiteralArgumentBuilder command) {
        final LiteralCommandNode build = command.build();
        this.root.addChild(build);
        return build;
    }

    public int execute(final String input, final CommandSource<?> source) throws CommandException {
        return execute(new StringReader(input), source);
    }

    public int execute(final StringReader input, final CommandSource<?> source) throws CommandException {
        final ParseResults parse = parse(input, source);
        return execute(parse);
    }

    public int execute(final ParseResults parse) throws CommandException {
        if (parse.reader().canRead()) {
            if (parse.exceptions().size() == 1) {
                throw parse.exceptions().values().iterator().next();
            } else if (parse.context().getRange().isEmpty()) {
                throw BuiltinExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
            } else {
                throw BuiltinExceptions.DISPATCHER_UNKNOWN_ARGUMENT.create();
            }
        }

        final String command = parse.reader().getString();
        final CommandContext context = parse.context().build(command);

        if (context.getCommand() == null) {
            throw BuiltinExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
        }

        CommandEvent event = new CommandEvent(context);
        EventManager.getInstance().broadcast(event, true);
        if (event.isCanceled()) {
            return 0;
        }

        return context.getCommand().run(context);
    }

    public ParseResults parse(final String command, final CommandSource<?> source) {
        return parse(new StringReader(command), source);
    }

    public ParseResults parse(final StringReader command, final CommandSource<?> source) {
        final CommandContextBuilder context = new CommandContextBuilder(this, source, this.root, command.getCursor());
        return parseNodes(this.root, command, context);
    }

    private ParseResults parseNodes(final CommandNode node, final StringReader originalReader, final CommandContextBuilder contextBuilder) {
        final CommandSource<?> source = contextBuilder.getSource();
        Map<CommandNode, CommandException> errors = new LinkedHashMap<>();
        List<ParseResults> potentials = new ArrayList<>(1);
        final int cursor = originalReader.getCursor();

        for (final CommandNode child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            final CommandContextBuilder context = contextBuilder.copy();
            final StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                } catch (final RuntimeException ex) {
                    throw BuiltinExceptions.DISPATCHER_PARSE_EXCEPTION.create(ex.getMessage());
                }
                if (reader.canRead()) {
                    if (reader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                        throw BuiltinExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR.create();
                    }
                }
            } catch (final CommandException ex) {
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }

            context.withCommand(child.getCommand());
            if (reader.canRead(2)) {
                reader.skip();
                final ParseResults parse = parseNodes(child, reader, context);
                potentials.add(parse);

            } else {
                potentials.add(new ParseResults(context, reader, Collections.emptyMap()));
            }
        }

        if (!potentials.isEmpty()) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.reader().canRead() && b.reader().canRead()) {
                        return -1;
                    }
                    if (a.reader().canRead() && !b.reader().canRead()) {
                        return 1;
                    }
                    if (a.exceptions().isEmpty() && !b.exceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.exceptions().isEmpty() && b.exceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.getFirst();
        }

        return new ParseResults(contextBuilder, originalReader, errors);
    }

    public String[] getAllUsage(final CommandNode node, final CommandSource<?> source, final boolean restricted) {
        final ArrayList<String> result = new ArrayList<>();
        getAllUsage(node, source, result, "", restricted);
        return result.toArray(new String[0]);
    }

    private void getAllUsage(final CommandNode node, final CommandSource<?> source, final ArrayList<String> result, final String prefix, final boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }

        if (node.getCommand() != null) {
            result.add(prefix);
        }

        if (!node.getChildren().isEmpty()) {
            for (final CommandNode child : node.getChildren()) {
                getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }

    public Map<CommandNode, String> getSmartUsage(final CommandNode node, final CommandSource<?> source) {
        final Map<CommandNode, String> result = new LinkedHashMap<>();

        final boolean optional = node.getCommand() != null;
        for (final CommandNode child : node.getChildren()) {
            final String usage = getSmartUsage(child, source, optional, false);
            if (usage != null) {
                result.put(child, usage);
            }
        }
        return result;
    }

    private String getSmartUsage(final CommandNode node, final CommandSource<?> source, final boolean optional, final boolean deep) {
        if (!node.canUse(source)) {
            return null;
        }

        final String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        final boolean childOptional = node.getCommand() != null;
        final String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        final String close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;

        if (!deep) {
            final Collection<CommandNode> children = node.getChildren().stream().filter(c -> c.canUse(source)).toList();
            if (children.size() == 1) {
                final String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + ARGUMENT_SEPARATOR + usage;
                }
            } else if (children.size() > 1) {
                final Set<String> childUsage = new LinkedHashSet<>();
                for (final CommandNode child : children) {
                    final String usage = getSmartUsage(child, source, childOptional, true);
                    if (usage != null) {
                        childUsage.add(usage);
                    }
                }
                if (childUsage.size() == 1) {
                    final String usage = childUsage.iterator().next();
                    return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE : usage);
                } else if (childUsage.size() > 1) {
                    final StringBuilder builder = new StringBuilder(open);
                    int count = 0;
                    for (final CommandNode child : children) {
                        if (count > 0) {
                            builder.append(USAGE_OR);
                        }
                        builder.append(child.getUsageText());
                        count++;
                    }
                    if (count > 0) {
                        builder.append(close);
                        return self + ARGUMENT_SEPARATOR + builder;
                    }
                }
            }
        }

        return self;
    }

    public RootCommandNode getRoot() {
        return root;
    }

    public Collection<String> getPath(final CommandNode target) {
        final List<List<CommandNode>> nodes = new ArrayList<>();
        addPaths(root, nodes, new ArrayList<>());

        for (final List<CommandNode> list : nodes) {
            if (list.get(list.size() - 1) == target) {
                final List<String> result = new ArrayList<>(list.size());
                for (final CommandNode node : list) {
                    if (node != root) {
                        result.add(node.getName());
                    }
                }
                return result;
            }
        }

        return Collections.emptyList();
    }

    public CommandNode findNode(final Collection<String> path) {
        CommandNode node = root;
        for (final String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    private void addPaths(final CommandNode node, final List<List<CommandNode>> result, final List<CommandNode> parents) {
        final List<CommandNode> current = new ArrayList<>(parents);
        current.add(node);
        result.add(current);

        for (final CommandNode child : node.getChildren()) {
            addPaths(child, result, current);
        }
    }

}
