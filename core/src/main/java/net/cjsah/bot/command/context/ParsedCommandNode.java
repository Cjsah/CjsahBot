package net.cjsah.bot.command.context;

import net.cjsah.bot.command.tree.CommandNode;

public record ParsedCommandNode(CommandNode node, StringRange range) {

    @Override
    public String toString() {
        return this.node + "@" + this.range;
    }
}
