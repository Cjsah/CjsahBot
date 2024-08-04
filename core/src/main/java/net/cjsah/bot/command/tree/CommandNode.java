package net.cjsah.bot.command.tree;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.builder.ArgumentBuilder;
import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class CommandNode implements Comparable<CommandNode> {
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode> arguments = new LinkedHashMap<>();
    private final Predicate<CommandSource<?>> requirement;
    @Nullable
    private Command command;
    private String help;

    public CommandNode(String help, Predicate<CommandSource<?>> requirement, @Nullable Command command) {
        this.requirement = requirement;
        this.command = command;
        this.help = help;
    }

    public abstract String getName();

    public abstract String getUsageText();

    protected abstract boolean isValidInput(String input);

    public abstract void parse(StringReader reader, ContextBuilder builder) throws CommandException;

    public abstract ArgumentBuilder<?> createBuilder();

    protected abstract String getSortedKey();

    public Collection<CommandNode> getChildren() {
        return this.children.values();
    }

    @Nullable
    public Command getCommand() {
        return this.command;
    }

    public String getHelp() {
        return this.help;
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    public boolean canUse(CommandSource<?> source) {
        return this.requirement.test(source);
    }

    public void addChild(CommandNode child) {
        if (child instanceof RootCommandNode) {
            throw new UnsupportedOperationException("无法将 'RootCommandNode' 作为一个子节点添加到其他 'CommandNode'");
        }
        CommandNode node = this.children.get(child.getName());
        if (node == null) {
            this.children.put(child.getName(), child);
            if (child instanceof LiteralCommandNode) {
                literals.put(child.getName(), (LiteralCommandNode) child);
            } else if (child instanceof ArgumentCommandNode) {
                arguments.put(child.getName(), (ArgumentCommandNode) child);
            }
        } else {
            node.help = child.help;
            if (child.command != null) {
                node.command = child.command;
            }
            child.getChildren().forEach(node::addChild);
        }
    }

    public Collection<? extends CommandNode> getRelevantNodes(StringReader input) {
        if (!this.literals.isEmpty()) {
            int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') input.skip();
            String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            LiteralCommandNode literal = this.literals.get(text);
            if (literal == null) return this.arguments.values();
            return Collections.singleton(literal);
        }
        return this.arguments.values();
    }

    @Override
    public int hashCode() {
        return 31 * this.children.hashCode() + (command != null ? command.hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandNode node)) return false;
        if (this.children != node.children) return false;
        return Objects.equals(command, node.command);
    }

    @Override
    public int compareTo(@NotNull CommandNode other) {
        if (this instanceof LiteralCommandNode == other instanceof LiteralCommandNode) {
            return this.getSortedKey().compareTo(other.getSortedKey());
        } else if (other instanceof LiteralCommandNode) {
            return 1;
        } else {
            return -1;
        }
    }
}
