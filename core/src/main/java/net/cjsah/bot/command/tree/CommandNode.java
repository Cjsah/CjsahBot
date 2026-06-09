package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public abstract class CommandNode {
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode<?>> arguments = new LinkedHashMap<>();
    private final Predicate<CommandSource<?>> requirement;
    private final Set<String> pluginIds;
    @Nullable
    private Command command;

    protected CommandNode(Collection<String> pluginIds, @Nullable Command command, Predicate<CommandSource<?>> requirement) {
        this.pluginIds = new HashSet<>(pluginIds);
        this.command = command;
        this.requirement = requirement;
    }

    public Command getCommand() {
        return this.command;
    }

    public Collection<CommandNode> getChildren() {
        return this.children.values();
    }

    public CommandNode getChild(String name) {
        return this.children.get(name);
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    public boolean canUse(final CommandSource<?> source) {
        return requirement.test(source);
    }

    public Set<String> getPluginIds() {
        return this.pluginIds;
    }

    public boolean containsPlugin(String pluginId) {
        return this.pluginIds.contains(pluginId);
    }

    public abstract String getName();

    public abstract String getUsageText();

    protected abstract boolean isValidInput(final String input);

    public abstract void parse(StringReader reader, CommandContextBuilder contextBuilder) throws CommandException;

    protected abstract ArgumentBuilder<?> builderFactory();

    public ArgumentBuilder<?> createBuilder() {
        ArgumentBuilder<?> builder = this.builderFactory();
        builder.requires(this.getRequirement());
        if (this.getCommand() != null) {
            builder.executes(this.getCommand());
        }
        if (this.getPluginIds() != null) {
            builder.byPlugins(this.getPluginIds());
        }
        return builder;
    }

    public void addChild(CommandNode node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        }

        final CommandNode child = this.children.get(node.getName());
        if (child != null) {
            // We've found something to merge onto
            if (node.getCommand() != null) {
                child.command = node.getCommand();
            }
            for (final CommandNode grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
        } else {
            this.children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                this.literals.put(node.getName(), (LiteralCommandNode) node);
            } else if (node instanceof ArgumentCommandNode) {
                this.arguments.put(node.getName(), (ArgumentCommandNode<?>) node);
            }
        }
    }

    public Collection<? extends CommandNode> getRelevantNodes(final StringReader input) {
        if (!this.literals.isEmpty()) {
            final int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            final String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            final LiteralCommandNode literal = this.literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            }
        }
        return arguments.values();
    }

    public void removePlugin(String pluginId) {

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandNode that)) return false;

        if (!this.children.equals(that.children)) return false;
        return Objects.equals(this.command, that.command);
    }

    @Override
    public int hashCode() {
        return 31 * this.children.hashCode() + (this.command != null ? this.command.hashCode() : 0);
    }

}
