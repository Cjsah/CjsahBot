package net.cjsah.bot.command;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.command.tree.RootCommandNode;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Dispatcher {
    private static final char ARGUMENT_SEPARATOR = ' ';
    private final CommandNode roots = new RootCommandNode();

    public void register(LiteralArgumentBuilder command) {
        this.roots.addChild(command.build());
    }

    public void execute(String input, CommandSource<?> source) throws CommandException {
        StringReader reader = new StringReader(input);
        ContextBuilder builder = new ContextBuilder(this, source, this.roots, reader.getCursor());
        ParseResults parse = this.parseNodes(this.roots, reader, builder);
        if (parse.reader().canRead()) {
            if (!parse.exceptions().isEmpty()) {
                throw parse.exceptions().values().iterator().next();
            } else if (parse.context().getRange().isEmpty()) {
                throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
            } else {
                throw BuiltExceptions.DISPATCHER_UNKNOWN_ARGUMENT.create();
            }
        }
        CommandContext original = parse.context().build();
        if (original.getCommand() == null) {
            throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
        }
        try {
            original.getCommand().run(original);
        } catch (CommandException e) {
            log.error("Command error", e);
        }
    }

    private ParseResults parseNodes(CommandNode node, StringReader originReader, ContextBuilder builder) {
        Map<CommandNode, CommandException> exceptions = new LinkedHashMap<>();
        CommandSource<?> source = builder.getSource();
        List<ParseResults> potentials = new ArrayList<>();
        int cursor = originReader.getCursor();
        for (CommandNode child : node.getRelevantNodes(originReader)) {
            if (!child.canUse(source)) continue;
            ContextBuilder context = builder.copy();
            StringReader reader = originReader.copy();
            try {
                child.parse(reader, context);
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR) {
                    throw BuiltExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR.create();
                }
            }catch (CommandException e) {
                exceptions.put(child, e);
                reader.setCursor(cursor);
                continue;
            }
            context.withCommand(child.getCommand());
            if (reader.canRead(2)) {
                reader.skip();
                ParseResults parse = this.parseNodes(child, reader, context);
                potentials.add(parse);
            } else {
                potentials.add(new ParseResults(context, reader, Collections.emptyMap()));
            }
        }
        if (!potentials.isEmpty()) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.reader().canRead() && b.reader().canRead()) {
                        return -1;
                    }
                    if (a.reader().canRead() && !b.reader().canRead()) {
                        return 1;
                    }
                    if (a.exceptions().isEmpty() && !b.exceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.exceptions().isEmpty() && b.exceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.getFirst();
        }
        return new ParseResults(builder, originReader, exceptions);
    }

    public Map<String, String> getHelp(CommandSource<?> source) {
        Map<String, String> result = new LinkedHashMap<>();
        this.roots.getChildren().forEach(it -> this.getHelp(result, "", it, source));
        return result;
    }

    private void getHelp(Map<String, String> result, String mem, CommandNode node, CommandSource<?> source) {
        if (!node.canUse(source)) return;
        mem += " " + node.getUsageText();
        if (node.getCommand() != null) {
            StringBuilder builder = new StringBuilder(mem);
            builder.setCharAt(0, '/');
            result.put(builder.toString(), node.getHelp());
        }
        String finalMem = mem;
        node.getChildren().forEach(it -> this.getHelp(result, finalMem, it, source));
    }
}
