package net.cjsah.bot.command.builder;


import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<T extends ArgumentBuilder<T>> {
    private final RootCommandNode arguments = new RootCommandNode();
    private Predicate<CommandSource<?>> requirement = CommandManager.passRequirement();
    private final Set<String> pluginIds = new HashSet<>();
    private Command command;

    protected abstract T getThis();

    public abstract CommandNode build();

    public T then(final ArgumentBuilder<?> argument) {
        this.arguments.addChild(argument.build());
        return getThis();
    }

    public T then(final CommandNode argument) {
        this.arguments.addChild(argument);
        return getThis();
    }

    public Collection<CommandNode> getArguments() {
        return this.arguments.getChildren();
    }

    public T executes(final Command command) {
        this.command = command;
        return getThis();
    }

    public Command getCommand() {
        return this.command;
    }

    public T requires(final Predicate<CommandSource<?>> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    public T byPlugin(String pluginId) {
        if (pluginId != null) {
            this.pluginIds.add(pluginId);
        }
        return getThis();
    }

    public T byPlugins(Collection<String> pluginIds) {
        this.pluginIds.addAll(pluginIds);
        return getThis();
    }

    @Nullable
    public Set<String> getPluginIds() {
        return this.pluginIds;
    }

    public boolean containsPlugin(String pluginId) {
        return this.pluginIds.contains(pluginId);
    }
}
