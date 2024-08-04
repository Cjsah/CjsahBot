package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<T extends ArgumentBuilder<T>> {
    private final CommandNode arguments = new RootCommandNode();
    private Predicate<CommandSource<?>> requirement = source -> true;
    @Nullable
    private Command command = null;
    private String help = "";

    public T then(ArgumentBuilder<?> argument) {
        this.arguments.addChild(argument.build());
        return getThis();
    }

    public T executes(String help, @NotNull Command command) {
        this.command = command;
        this.help = help;
        return getThis();
    }

    public T requires(Predicate<CommandSource<?>> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    protected abstract T getThis();

    public abstract CommandNode build();

    public Collection<CommandNode> getArguments() {
        return this.arguments.getChildren();
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    @Nullable
    public Command getCommand() {
        return this.command;
    }

    public String getHelp() {
        return this.help;
    }
}
