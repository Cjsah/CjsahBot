package net.cjsah.bot.exception;

public final class BuiltExceptions{
    public static final CommandExceptionFactory DOUBLE_TOO_LOW = new CommandExceptionFactory("Double 必须小于 %2$d, 实际为 %1$d");
    public static final CommandExceptionFactory DOUBLE_TOO_HIGH = new CommandExceptionFactory("Double 必须大于 %2$d, 实际为 %1$d");

    public static final CommandExceptionFactory FLOAT_TOO_LOW = new CommandExceptionFactory("Float 必须小于 %2$d, 实际为 %1$d");
    public static final CommandExceptionFactory FLOAT_TOO_HIGH = new CommandExceptionFactory("Float 必须大于 %2$d, 实际为 %1$d");

    public static final CommandExceptionFactory INTEGER_TOO_LOW = new CommandExceptionFactory("Integer 必须小于 %2$d, 实际为 %1$d");
    public static final CommandExceptionFactory INTEGER_TOO_HIGH = new CommandExceptionFactory("Integer 必须大于 %2$d, 实际为 %1$d");

    public static final CommandExceptionFactory LONG_TOO_LOW = new CommandExceptionFactory("Long 必须小于 %2$d, 实际为 %1$d");
    public static final CommandExceptionFactory LONG_TOO_HIGH = new CommandExceptionFactory("Long 必须大于 %2$d, 实际为 %1$d");

    public static final CommandExceptionFactory LITERAL_INCORRECT = new CommandExceptionFactory("预期 %s");

    public static final CommandExceptionFactory READER_EXPECTED_START_OF_QUOTE = new CommandExceptionFactory("字符串预期以引号开始");
    public static final CommandExceptionFactory READER_EXPECTED_END_OF_QUOTE = new CommandExceptionFactory("字符串未以引号闭合");

    public static final CommandExceptionFactory READER_INVALID_ESCAPE = new CommandExceptionFactory("在字符串中的转义 '%c' 无效");
    public static final CommandExceptionFactory READER_INVALID_BOOL = new CommandExceptionFactory("无效的 Bool, 预期为 'True' 或 'False' 实际为 '%s'");
    public static final CommandExceptionFactory READER_INVALID_INT = new CommandExceptionFactory("无效的 integer '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_INT = new CommandExceptionFactory("无效的 integer");
    public static final CommandExceptionFactory READER_INVALID_BYTE = new CommandExceptionFactory("无效的 byte '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_BYTE = new CommandExceptionFactory("无效的 byte");
    public static final CommandExceptionFactory READER_INVALID_SHORT = new CommandExceptionFactory("无效的 short '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_SHORT = new CommandExceptionFactory("无效的 short");
    public static final CommandExceptionFactory READER_INVALID_LONG = new CommandExceptionFactory("无效的 long '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_LONG = new CommandExceptionFactory("无效的 long");
    public static final CommandExceptionFactory READER_INVALID_DOUBLE = new CommandExceptionFactory("无效的 double '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_DOUBLE = new CommandExceptionFactory("无效的 double");
    public static final CommandExceptionFactory READER_INVALID_FLOAT = new CommandExceptionFactory("无效的 float '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_FLOAT = new CommandExceptionFactory("无效的 float");
    public static final CommandExceptionFactory READER_EXPECTED_BOOL = new CommandExceptionFactory("无效的 bool");
    public static final CommandExceptionFactory READER_EXPECTED_SYMBOL = new CommandExceptionFactory("无效的 '%c'");

    public static final CommandExceptionFactory DISPATCHER_UNKNOWN_COMMAND = new CommandExceptionFactory("未知指令");
    public static final CommandExceptionFactory DISPATCHER_COMMAND_NO_PERMISSION = new CommandExceptionFactory("没有权限执行此命令");
    public static final CommandExceptionFactory DISPATCHER_UNKNOWN_ARGUMENT = new CommandExceptionFactory("参数错误 / 没有权限");
    public static final CommandExceptionFactory COMMAND_PATTERN_ERROR = new CommandExceptionFactory("命令格式错误: %s");
    public static final CommandExceptionFactory DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new CommandExceptionFactory("需要空格来结束一个参数，但发现尾随数据");
    public static final CommandExceptionFactory ERROR_PARSE_PARAM_NAME = new CommandExceptionFactory("错误的参数名称: %s");
    public static final CommandExceptionFactory NO_PARAM_NAME = new CommandExceptionFactory("第 %d 个参数没有填写名称");
    public static final CommandExceptionFactory EXPECTED_STRING_MAP = new CommandExceptionFactory("只允许Map类型为 Map<String,String>");
    public static final CommandExceptionFactory REPEAT_COMMAND = new CommandExceptionFactory("此命令已注册");
    public static final CommandExceptionFactory DISPATCHER_PARSE_EXCEPTION = new CommandExceptionFactory("无法解析命令: '%s'");

    public static final CommandExceptionFactory PLUGIN_NOT_FOUND = new CommandExceptionFactory("没有找到插件: '%s'");
    public static final CommandExceptionFactory REGISTER_IN_PLUGIN = new CommandExceptionFactory("请在插件中注册命令");
    public static final CommandExceptionFactory UNSUPPORTED_TYPE  = new CommandExceptionFactory("不支持的类型: %s");

    public static final CustomExceptionFactory<?> REQUEST_FAILED = new CustomExceptionFactory<>("请求失败: %s", RequestException::new);

    public static final CustomExceptionFactory<?> CONFLICT_PERMISSION = new CustomExceptionFactory<>("%s 与 %s 权限冲突", PermissionException::new);
    public static final CustomExceptionFactory<?> UNKNOWN_PERMISSION_TYPE = new CustomExceptionFactory<>("未知的权限类型: %s", PermissionException::new);
}
