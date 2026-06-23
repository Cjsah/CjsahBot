package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.tree.ArgumentCommandNode;
import net.cjsah.bot.command.tree.CommandNode;
import org.jetbrains.annotations.Nullable;

public class RequiredArgumentBuilder<T> extends ArgumentBuilder<RequiredArgumentBuilder<T>> {
    private final String name;
    private final Argument<T> argument;

    private RequiredArgumentBuilder(@Nullable final String pluginId, final String name, final Argument<T> argument) {
        this.name = name;
        this.argument = argument;
        this.byPlugin(pluginId);
    }

    public static <T> RequiredArgumentBuilder<T> argument(final String pluginId, final String name, final Argument<T> argument) {
        return new RequiredArgumentBuilder<>(pluginId, name, argument);
    }

    public String getName() {
        return this.name;
    }

    public Argument<T> getArgument() {
        return this.argument;
    }

    @Override
    protected RequiredArgumentBuilder<T> getThis() {
        return this;
    }

    @Override
    public ArgumentCommandNode<T> build() {
        final ArgumentCommandNode<T> result = new ArgumentCommandNode<>(
            this.getName(),
            this.getArgument(),
            this.getPluginIds(),
            this.getCommand(),
            this.getRequirement(),
            this.getDescription()
        );

        for (final CommandNode argument : this.getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
