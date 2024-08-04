package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.tree.ArgumentCommandNode;
import net.cjsah.bot.command.tree.CommandNode;

public class RequiredArgumentBuilder<T> extends ArgumentBuilder<RequiredArgumentBuilder<T>> {
    private final String name;
    private final Argument<T> type;

    private RequiredArgumentBuilder(String name, Argument<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <S> RequiredArgumentBuilder<S> argument(String name, Argument<S> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    @Override
    protected RequiredArgumentBuilder<T> getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        CommandNode node = new ArgumentCommandNode<>(
                this.getHelp(),
                this.name,
                this.type,
                this.getRequirement(),
                this.getCommand()
        );
        this.getArguments().forEach(node::addChild);
        return node;
    }
}
