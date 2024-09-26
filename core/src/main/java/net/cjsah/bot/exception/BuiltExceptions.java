package net.cjsah.bot.exception;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class BuiltExceptions{
    public static final Para2CommandException DOUBLE_TOO_LOW = new Para2CommandException((found, min) -> "Double 必须小于 " + min + ", 但是发现了 " + found);
    public static final Para2CommandException DOUBLE_TOO_HIGH = new Para2CommandException((found, max) -> "Double 必须大于 " + max + ", 但是发现了 " + found);

    public static final Para2CommandException FLOAT_TOO_LOW = new Para2CommandException((found, min) -> "Float 必须小于 " + min + ", 但是发现了 " + found);
    public static final Para2CommandException FLOAT_TOO_HIGH = new Para2CommandException((found, max) -> "Float 必须大于 " + max + ", 但是发现了 " + found);

    public static final Para2CommandException INTEGER_TOO_LOW = new Para2CommandException((found, min) -> "Integer 必须小于 " + min + ", 但是发现了 " + found);
    public static final Para2CommandException INTEGER_TOO_HIGH = new Para2CommandException((found, max) -> "Integer 必须大于 " + max + ", 但是发现了 " + found);

    public static final Para2CommandException LONG_TOO_LOW = new Para2CommandException((found, min) -> "Long 必须小于 " + min + ", 但是发现了 " + found);
    public static final Para2CommandException LONG_TOO_HIGH = new Para2CommandException((found, max) -> "Long 必须大于 " + max + ", 但是发现了 " + found);

    public static final Para1CommandException LITERAL_INCORRECT = new Para1CommandException((expected) -> "预期 " + expected);

    public static final Para0CommandException READER_EXPECTED_START_OF_QUOTE = new Para0CommandException("字符串预期以引号开始");
    public static final Para0CommandException READER_EXPECTED_END_OF_QUOTE = new Para0CommandException("字符串未以引号闭合");

    public static final Para1CommandException READER_INVALID_ESCAPE = new Para1CommandException((character) -> "在字符串中的转义 '" + character + "' 无效");
    public static final Para1CommandException READER_INVALID_BOOL = new Para1CommandException((value) -> "无效的 Bool, 预期为 'true' 或 'false' 但是发现了 '" + value + "'");
    public static final Para1CommandException READER_INVALID_INT = new Para1CommandException((value) -> "无效的 integer '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_INT = new Para0CommandException("无效的 integer");
    public static final Para1CommandException READER_INVALID_BYTE = new Para1CommandException((value) -> "无效的 byte '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_BYTE = new Para0CommandException("无效的 byte");
    public static final Para1CommandException READER_INVALID_SHORT = new Para1CommandException((value) -> "无效的 short '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_SHORT = new Para0CommandException("无效的 short");
    public static final Para1CommandException READER_INVALID_LONG = new Para1CommandException((value) -> "无效的 long '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_LONG = new Para0CommandException("无效的 long");
    public static final Para1CommandException READER_INVALID_DOUBLE = new Para1CommandException((value) -> "无效的 double '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_DOUBLE = new Para0CommandException("无效的 double");
    public static final Para1CommandException READER_INVALID_FLOAT = new Para1CommandException((value) -> "无效的 float '" + value + "'");
    public static final Para0CommandException READER_EXPECTED_FLOAT = new Para0CommandException("无效的 float");
    public static final Para0CommandException READER_EXPECTED_BOOL = new Para0CommandException("无效的 bool");
    public static final Para1CommandException READER_EXPECTED_SYMBOL = new Para1CommandException((symbol) -> "无效的 '" + symbol + "'");

    public static final Para0CommandException DISPATCHER_UNKNOWN_COMMAND = new Para0CommandException("未知指令");
    public static final Para0CommandException DISPATCHER_UNKNOWN_ARGUMENT = new Para0CommandException("参数错误 / 没有权限");
    public static final Para1CommandException COMMAND_PATTERN_ERROR = new Para1CommandException((cmd) -> "命令格式错误: " + cmd);
    public static final Para0CommandException DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new Para0CommandException("需要空格来结束一个参数，但发现尾随数据");
    public static final Para1CommandException ERROR_PARSE_PARAM_NAME = new Para1CommandException(param -> "错误的参数名称: " + param);
    public static final Para1CommandException ERROR_PARAM_MISSING = new Para1CommandException(index -> "参数数量不足: 需要第 " + index + " 个参数的名称");
    public static final Para1CommandException DISPATCHER_PARSE_EXCEPTION = new Para1CommandException((message) -> "无法解析命令: '" + message + "'");

    public static final Para1CommandException PLUGIN_NOT_FOUND = new Para1CommandException((plugin) -> "没有找到此插件: '" + plugin + "'");

    public static final Para0CommandException REGISTER_IN_PLUGIN = new Para0CommandException("请在插件中注册命令");

    public static final Function<String, RequestException> REQUEST_FAILED = (msg) -> new RequestException("请求失败: " + msg);

    public static final Para1CommandException UNSUPPORTED_TYPE  = new Para1CommandException((type) -> "不支持的类型: " + type);

    public static final BiFunction<String, String, PermissionException> CONFLICT_PERMISSION = (p1, p2) -> new PermissionException(p1 + " 与 " + p2 + " 权限冲突");
    public static final Function<String, PermissionException> UNKNOWN_PERMISSION_TYPE = (type) -> new PermissionException("未知的权限类型: " + type);
}
