package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class CommandNode<S> {
    private final Map<String, CommandNode<S>> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap<>();
    private final Predicate<S> requirement;
    @Nullable
    private final String pluginId;
    @Nullable
    private Command<S> command;

    protected CommandNode(@Nullable String pluginId, @Nullable Command<S> command, Predicate<S> requirement) {
        this.pluginId = pluginId;
        this.command = command;
        this.requirement = requirement;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public Collection<CommandNode<S>> getChildren() {
        return this.children.values();
    }

    public CommandNode<S> getChild(String name) {
        return this.children.get(name);
    }

    public Predicate<S> getRequirement() {
        return this.requirement;
    }

    public boolean canUse(final S source) {
        return requirement.test(source);
    }

    public String getPluginId() {
        return this.pluginId;
    }

    public abstract String getName();

    public abstract String getUsageText();

    protected abstract boolean isValidInput(final String input);

    public abstract void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandException;

    protected abstract ArgumentBuilder<S, ?> builderFactory();

    public ArgumentBuilder<S, ?> createBuilder() {
        ArgumentBuilder<S, ?> builder = this.builderFactory();
        builder.requires(this.getRequirement());
        if (this.getCommand() != null) {
            builder.executes(this.getCommand());
        }
        if (this.getPluginId() != null) {
            builder.byPlugin(this.getPluginId());
        }
        return builder;
    }

    public void addChild(CommandNode<S> node) {
        if (node instanceof RootCommandNode<S>) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        }

        final CommandNode<S> child = this.children.get(node.getName());
        if (child != null) {
            // We've found something to merge onto
            if (node.getCommand() != null) {
                child.command = node.getCommand();
            }
            for (final CommandNode<S> grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
        } else {
            this.children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                this.literals.put(node.getName(), (LiteralCommandNode<S>) node);
            } else if (node instanceof ArgumentCommandNode) {
                this.arguments.put(node.getName(), (ArgumentCommandNode<S, ?>) node);
            }
        }
    }

    public Collection<? extends CommandNode<S>> getRelevantNodes(final StringReader input) {
        if (!this.literals.isEmpty()) {
            final int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            final String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            final LiteralCommandNode<S> literal = this.literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            }
        }
        return arguments.values();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandNode<?> that)) return false;

        if (!this.children.equals(that.children)) return false;
        return Objects.equals(this.command, that.command);
    }

    @Override
    public int hashCode() {
        return 31 * this.children.hashCode() + (this.command != null ? this.command.hashCode() : 0);
    }

}
