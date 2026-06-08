package net.cjsah.bot.command;

import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.LiteralCommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandDispatcher<S> {
    public static final String ARGUMENT_SEPARATOR = " ";

    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";

    private final RootCommandNode<S> root;

    private final Predicate<CommandNode<S>> hasCommand = new Predicate<>() {
        @Override
        public boolean test(final CommandNode<S> input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(hasCommand));
        }
    };

    public CommandDispatcher(final RootCommandNode<S> root) {
        this.root = root;
    }

    public CommandDispatcher() {
        this(new RootCommandNode<>());
    }

    public LiteralCommandNode<S> register(final LiteralArgumentBuilder<S> command) {
        final LiteralCommandNode<S> build = command.build();
        this.root.addChild(build);
        return build;
    }

    public int execute(final String input, final S source) throws CommandException {
        return execute(new StringReader(input), source);
    }

    public int execute(final StringReader input, final S source) throws CommandException {
        final ParseResults<S> parse = parse(input, source);
        return execute(parse);
    }

    public int execute(final ParseResults<S> parse) throws CommandException {
        if (parse.reader().canRead()) {
            if (parse.exceptions().size() == 1) {
                throw parse.exceptions().values().iterator().next();
            } else if (parse.context().getRange().isEmpty()) {
                throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
            } else {
                throw BuiltExceptions.DISPATCHER_UNKNOWN_ARGUMENT.create();
            }
        }

        final String command = parse.reader().getString();
        final CommandContext<S> context = parse.context().build(command);

        if (context.getCommand() == null) {
            throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
        }

        return context.getCommand().run(context);
    }

    public ParseResults<S> parse(final String command, final S source) {
        return parse(new StringReader(command), source);
    }

    public ParseResults<S> parse(final StringReader command, final S source) {
        final CommandContextBuilder<S> context = new CommandContextBuilder<>(this, source, this.root, command.getCursor());
        return parseNodes(this.root, command, context);
    }

    private ParseResults<S> parseNodes(final CommandNode<S> node, final StringReader originalReader, final CommandContextBuilder<S> contextBuilder) {
        final S source = contextBuilder.getSource();
        Map<CommandNode<S>, CommandException> errors = new LinkedHashMap<>();
        List<ParseResults<S>> potentials = new ArrayList<>(1);
        final int cursor = originalReader.getCursor();

        for (final CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            final CommandContextBuilder<S> context = contextBuilder.copy();
            final StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                } catch (final RuntimeException ex) {
                    throw BuiltExceptions.DISPATCHER_PARSE_EXCEPTION.create(ex.getMessage());
                }
                if (reader.canRead()) {
                    if (reader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                        throw BuiltExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR.create();
                    }
                }
            } catch (final CommandException ex) {
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }

            context.withCommand(child.getCommand());
            if (reader.canRead(2)) {
                final ParseResults<S> parse = parseNodes(child, reader, context);
                potentials.add(parse);

            } else {
                potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
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

        return new ParseResults<>(contextBuilder, originalReader, errors);
    }

    public String[] getAllUsage(final CommandNode<S> node, final S source, final boolean restricted) {
        final ArrayList<String> result = new ArrayList<>();
        getAllUsage(node, source, result, "", restricted);
        return result.toArray(new String[0]);
    }

    private void getAllUsage(final CommandNode<S> node, final S source, final ArrayList<String> result, final String prefix, final boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }

        if (node.getCommand() != null) {
            result.add(prefix);
        }

        if (!node.getChildren().isEmpty()) {
            for (final CommandNode<S> child : node.getChildren()) {
                getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }

    public Map<CommandNode<S>, String> getSmartUsage(final CommandNode<S> node, final S source) {
        final Map<CommandNode<S>, String> result = new LinkedHashMap<>();

        final boolean optional = node.getCommand() != null;
        for (final CommandNode<S> child : node.getChildren()) {
            final String usage = getSmartUsage(child, source, optional, false);
            if (usage != null) {
                result.put(child, usage);
            }
        }
        return result;
    }

    private String getSmartUsage(final CommandNode<S> node, final S source, final boolean optional, final boolean deep) {
        if (!node.canUse(source)) {
            return null;
        }

        final String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        final boolean childOptional = node.getCommand() != null;
        final String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        final String close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;

        if (!deep) {
            final Collection<CommandNode<S>> children = node.getChildren().stream().filter(c -> c.canUse(source)).collect(Collectors.toList());
            if (children.size() == 1) {
                final String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + ARGUMENT_SEPARATOR + usage;
                }
            } else if (children.size() > 1) {
                final Set<String> childUsage = new LinkedHashSet<>();
                for (final CommandNode<S> child : children) {
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
                    for (final CommandNode<S> child : children) {
                        if (count > 0) {
                            builder.append(USAGE_OR);
                        }
                        builder.append(child.getUsageText());
                        count++;
                    }
                    if (count > 0) {
                        builder.append(close);
                        return self + ARGUMENT_SEPARATOR + builder.toString();
                    }
                }
            }
        }

        return self;
    }

    public RootCommandNode<S> getRoot() {
        return root;
    }

    public Collection<String> getPath(final CommandNode<S> target) {
        final List<List<CommandNode<S>>> nodes = new ArrayList<>();
        addPaths(root, nodes, new ArrayList<>());

        for (final List<CommandNode<S>> list : nodes) {
            if (list.get(list.size() - 1) == target) {
                final List<String> result = new ArrayList<>(list.size());
                for (final CommandNode<S> node : list) {
                    if (node != root) {
                        result.add(node.getName());
                    }
                }
                return result;
            }
        }

        return Collections.emptyList();
    }

    public CommandNode<S> findNode(final Collection<String> path) {
        CommandNode<S> node = root;
        for (final String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    private void addPaths(final CommandNode<S> node, final List<List<CommandNode<S>>> result, final List<CommandNode<S>> parents) {
        final List<CommandNode<S>> current = new ArrayList<>(parents);
        current.add(node);
        result.add(current);

        for (final CommandNode<S> child : node.getChildren()) {
            addPaths(child, result, current);
        }
    }

}
