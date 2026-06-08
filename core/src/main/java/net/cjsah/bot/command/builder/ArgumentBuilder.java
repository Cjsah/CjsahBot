package net.cjsah.bot.command.builder;


import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.execute.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<T extends ArgumentBuilder<T>> {
    private final RootCommandNode arguments = new RootCommandNode();
    private Predicate<CommandSource<?>> requirement = CommandManager.passRequirement();
    private Command command;
    private String pluginId;

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
        this.pluginId = pluginId;
        return getThis();
    }

    @Nullable
    public String getPluginId() {
        return this.pluginId;
    }
}
