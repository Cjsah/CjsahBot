package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.LiteralCommandNode;
import org.jetbrains.annotations.Nullable;

public class LiteralArgumentBuilder extends ArgumentBuilder<LiteralArgumentBuilder> {
    private final String literal;

    private LiteralArgumentBuilder(@Nullable final String pluginId, final String literal) {
        this.literal = literal;
        this.byPlugin(pluginId);
    }

    public static LiteralArgumentBuilder literal(String pluginId, final String name) {
        return new LiteralArgumentBuilder(pluginId, name);
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    protected LiteralArgumentBuilder getThis() {
        return this;
    }

    @Override
    public LiteralCommandNode build() {
        final LiteralCommandNode result = new LiteralCommandNode(
            this.getLiteral(),
            this.getPluginIds(),
            this.getCommand(),
            this.getRequirement()
        );

        for (final CommandNode argument : this.getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
