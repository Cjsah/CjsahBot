package net.cjsah.bot.command.builder;

import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.LiteralCommandNode;

public class LiteralArgumentBuilder extends ArgumentBuilder<LiteralArgumentBuilder> {
    private final String literal;

    public LiteralArgumentBuilder(String literal) {
        this.literal = literal;
    }

    public static LiteralArgumentBuilder literal(String literal) {
        return new LiteralArgumentBuilder(literal);
    }

    @Override
    protected LiteralArgumentBuilder getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        LiteralCommandNode node = new LiteralCommandNode(
                this.getHelp(),
                this.literal,
                this.getRequirement(),
                this.getCommand()
        );
        this.getArguments().forEach(node::addChild);
        return node;
    }
}
