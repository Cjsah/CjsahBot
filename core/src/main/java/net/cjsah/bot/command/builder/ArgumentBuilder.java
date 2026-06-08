package net.cjsah.bot.command.builder;


import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private final RootCommandNode<S> arguments = new RootCommandNode<>();
    private Predicate<S> requirement = CommandManager.passRequirement();
    private Command<S> command;
    private String pluginId;

    protected abstract T getThis();

    public abstract CommandNode<S> build();

    public T then(final ArgumentBuilder<S, ?> argument) {
        this.arguments.addChild(argument.build());
        return getThis();
    }

    public T then(final CommandNode<S> argument) {
        this.arguments.addChild(argument);
        return getThis();
    }

    public Collection<CommandNode<S>> getArguments() {
        return this.arguments.getChildren();
    }

    public T executes(final Command<S> command) {
        this.command = command;
        return getThis();
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public T requires(final Predicate<S> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public Predicate<S> getRequirement() {
        return this.requirement;
    }

    public T byPlugin(String pluginId) {
        this.pluginId = pluginId;
        return getThis();
    }

    @Nullable
    public String getPluginId() {
        return this.pluginId;
    }
}
