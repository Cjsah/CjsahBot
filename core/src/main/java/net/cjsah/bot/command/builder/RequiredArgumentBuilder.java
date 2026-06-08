package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.tree.ArgumentCommandNode;
import net.cjsah.bot.command.tree.CommandNode;

public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    private final String name;
    private final Argument<T> argument;

    private RequiredArgumentBuilder(final String pluginId, final String name, final Argument<T> argument) {
        this.name = name;
        this.argument = argument;
        this.byPlugin(pluginId);
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(final String pluginId, final String name, final Argument<T> argument) {
        return new RequiredArgumentBuilder<>(pluginId, name, argument);
    }

    public String getName() {
        return this.name;
    }

    public Argument<T> getArgument() {
        return this.argument;
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    @Override
    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(
            this.getName(),
            this.getArgument(),
            this.getPluginId(),
            this.getCommand(),
            this.getRequirement()
        );

        for (final CommandNode<S> argument : this.getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
