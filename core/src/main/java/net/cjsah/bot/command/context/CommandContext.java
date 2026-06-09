package net.cjsah.bot.command.context;

import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CommandContext {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

    static {
        PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
    }

    private final CommandSource<?> source;
    private final String input;
    private final Command command;
    private final Map<String, ParsedArgument<?>> arguments;
    private final CommandNode rootNode;
    private final List<ParsedCommandNode> nodes;
    private final StringRange range;

    public CommandContext(final CommandSource<?> source, final String input, final Map<String, ParsedArgument<?>> arguments, final Command command, final CommandNode rootNode, final List<ParsedCommandNode> nodes, final StringRange range) {
        this.source = source;
        this.input = input;
        this.arguments = arguments;
        this.command = command;
        this.rootNode = rootNode;
        this.nodes = nodes;
        this.range = range;
    }

    public CommandContext copyFor(final CommandSource<?> source) {
        if (this.source == source) {
            return this;
        }
        return new CommandContext(source, this.input, this.arguments, this.command, this.rootNode, this.nodes, this.range);
    }

    public Command getCommand() {
        return this.command;
    }

    public CommandSource<?> getSource() {
        return this.source;
    }

    @SuppressWarnings("unchecked")
    public <V> Optional<V> getArgument(final String name, final Class<V> clazz) {
        final ParsedArgument<?> argument = this.arguments.get(name);

        if (argument == null) {
            throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
        }

        final Object result = argument.getResult();
        if (PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz).isAssignableFrom(result.getClass())) {
            return Optional.of((V) result);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandContext that)) return false;

        if (!this.arguments.equals(that.arguments)) return false;
        if (!this.rootNode.equals(that.rootNode)) return false;
        if (this.nodes.size() != that.nodes.size() || !nodes.equals(that.nodes)) return false;
        if (!Objects.equals(this.command, that.command)) return false;
        if (!this.source.equals(that.source)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.source.hashCode();
        result = 31 * result + this.arguments.hashCode();
        result = 31 * result + (this.command != null ? this.command.hashCode() : 0);
        result = 31 * result + this.rootNode.hashCode();
        result = 31 * result + this.nodes.hashCode();
        return result;
    }

    public StringRange getRange() {
        return this.range;
    }

    public String getInput() {
        return this.input;
    }

    public CommandNode getRootNode() {
        return this.rootNode;
    }

    public List<ParsedCommandNode> getNodes() {
        return this.nodes;
    }

    public boolean hasNodes() {
        return !this.nodes.isEmpty();
    }
}
