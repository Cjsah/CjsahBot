package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.LiteralCommandNode;

public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    private final String literal;

    private LiteralArgumentBuilder(final String pluginId, final String literal) {
        this.literal = literal;
        this.byPlugin(pluginId);
    }

    public static <S> LiteralArgumentBuilder<S> literal(String pluginId, final String name) {
        return new LiteralArgumentBuilder<>(pluginId, name);
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    protected LiteralArgumentBuilder<S> getThis() {
        return this;
    }

    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(
            this.getLiteral(),
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
