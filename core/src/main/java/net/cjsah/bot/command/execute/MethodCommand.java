package net.cjsah.bot.command.execute;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.simple.ParamInfo;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MethodCommand implements Command {
    private static final Map<Class<?>, Object> PRIMITIVE_DEFAULT_VAL = new HashMap<>();

    static {
        PRIMITIVE_DEFAULT_VAL.put(boolean.class, false);
        PRIMITIVE_DEFAULT_VAL.put(byte.class, (byte) 0);
        PRIMITIVE_DEFAULT_VAL.put(short.class, (short) 0);
        PRIMITIVE_DEFAULT_VAL.put(char.class, (char) 0);
        PRIMITIVE_DEFAULT_VAL.put(int.class, 0);
        PRIMITIVE_DEFAULT_VAL.put(long.class, 0L);
        PRIMITIVE_DEFAULT_VAL.put(float.class, 0F);
        PRIMITIVE_DEFAULT_VAL.put(double.class, .0);
    }

    private final Method method;
    List<ParamInfo> parameters;

    public MethodCommand(Method method, List<ParamInfo> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public int run(CommandContext context) throws CommandException {
        Object[] params = this.parameters
            .stream()
            .map(it -> {
                Object result = it.factory().apply(context);
                if (result == null) {
                    result = PRIMITIVE_DEFAULT_VAL.getOrDefault(it.type(), result);
                }
                return result;
            })
            .toArray();

        try {
            this.method.invoke(null, params);
            return 1;
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw BuiltinExceptions.FAILED_ACCESS_METHOD.create(e.getMessage());
        }
    }
}
