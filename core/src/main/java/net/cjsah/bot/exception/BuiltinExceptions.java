package net.cjsah.bot.exception;

public final class BuiltinExceptions {
    public static final CommandExceptionFactory READER_INVALID_BOOL = new CommandExceptionFactory("无效的 Bool, 预期为 'true' 或 'false' 实际为 '%s'");
    public static final CommandExceptionFactory READER_EXPECTED_BOOL = new CommandExceptionFactory("无效的 bool");
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

    public static final CommandExceptionFactory LITERAL_INCORRECT = new CommandExceptionFactory("Expected literal %s");

    public static final CommandExceptionFactory READER_EXPECTED_START_OF_QUOTE = new CommandExceptionFactory("Expected quote to start a string");
    public static final CommandExceptionFactory READER_EXPECTED_START_OF_PAIR = new CommandExceptionFactory("Expected quote to start a pair");
    public static final CommandExceptionFactory READER_INVALID_ESCAPE = new CommandExceptionFactory("Invalid escape sequence '%s' in quoted string");
    public static final CommandExceptionFactory READER_EXPECTED_END_OF_QUOTE = new CommandExceptionFactory("Unclosed quoted string");
    public static final CommandExceptionFactory READER_EXPECTED_SYMBOL = new CommandExceptionFactory("Expected '%s'");
    public static final CommandExceptionFactory NOT_EXPECTED = new CommandExceptionFactory("Expected '%s', but founded '%s'");

    public static final CommandExceptionFactory DISPATCHER_UNKNOWN_COMMAND = new CommandExceptionFactory("未知的命令或权限不足");
    public static final CommandExceptionFactory DISPATCHER_UNKNOWN_ARGUMENT = new CommandExceptionFactory("参数错误 / 没有权限");
    public static final CommandExceptionFactory DISPATCHER_PARSE_EXCEPTION = new CommandExceptionFactory("无法解析命令: '%s'");
    public static final CommandExceptionFactory DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new CommandExceptionFactory("Expected whitespace to end one argument, but found trailing data");

    public static final CommandExceptionFactory PARSE_EMPTY_STRING = new CommandExceptionFactory("Cannot parse an empty string");
    public static final CommandExceptionFactory PARSE_ROOT_ARGUMENT = new CommandExceptionFactory("The root node must be a literal node");
    public static final CommandExceptionFactory PARSE_ARGUMENT_NOT_EXIST = new CommandExceptionFactory("Argument type: %s does not exist");
    public static final CommandExceptionFactory PARSE_INVALID_NODE = new CommandExceptionFactory("Invalid node: %s");
    public static final CommandExceptionFactory FAILED_ACCESS_METHOD = new CommandExceptionFactory("Failed to access method: %s");

    public static final CommandExceptionFactory DISPATCHER_COMMAND_NO_PERMISSION = new CommandExceptionFactory("没有权限执行此命令");
    public static final CommandExceptionFactory COMMAND_PATTERN_ERROR = new CommandExceptionFactory("命令格式错误: %s");
    public static final CommandExceptionFactory ERROR_PARSE_PARAM_NAME = new CommandExceptionFactory("错误的参数名称: %s");
    public static final CommandExceptionFactory NO_PARAM_NAME = new CommandExceptionFactory("第 %d 个参数没有填写名称");
    public static final CommandExceptionFactory EXPECTED_STRING_MAP = new CommandExceptionFactory("只允许Map类型为 Map<String,String>");
    public static final CommandExceptionFactory REPEAT_COMMAND = new CommandExceptionFactory("此命令已注册");

    public static final CustomRuntimeExceptionFactory<?> NOT_IN_PLUGIN = CustomRuntimeExceptionFactory.runtime("请在插件线程上下文中注册", PluginException::new);
    public static final CustomRuntimeExceptionFactory<?> PLUGIN_NOT_FOUND = CustomRuntimeExceptionFactory.runtime("没有找到插件: '%s'", PluginException::new);

    public static final CommandExceptionFactory REGISTER_IN_PLUGIN = new CommandExceptionFactory("请在插件中注册命令");
    public static final CommandExceptionFactory UNSUPPORTED_TYPE = new CommandExceptionFactory("不支持的类型: %s");

    public static final CustomRuntimeExceptionFactory<?> APP_NOT_INIT = new CustomRuntimeExceptionFactory<>("事件还没有初始化!", AppException::new);
    public static final CustomRuntimeExceptionFactory<?> EVENT_NOT_INIT = CustomRuntimeExceptionFactory.runtime("事件还没有初始化!", EventException::new);

    public static final CustomRuntimeExceptionFactory<?> REQUEST_FAILED = new CustomRuntimeExceptionFactory<>("请求失败: %s", RequestException::new);

    public static final CustomRuntimeExceptionFactory<?> CONFLICT_PERMISSION = new CustomRuntimeExceptionFactory<>("%s 与已有权限冲突", PermissionException::new);
    public static final CustomRuntimeExceptionFactory<?> CONFLICT_MERGE_PERMISSION = new CustomRuntimeExceptionFactory<>("%s已为 %s模式, 但受到了 %s 数据", PermissionException::new);
    public static final CustomRuntimeExceptionFactory<?> UNKNOWN_PERMISSION_TYPE = new CustomRuntimeExceptionFactory<>("未知的权限类型: %s", PermissionException::new);

    public static final CustomRuntimeExceptionFactory<?> MSG_TOO_MANY_DATA = new CustomRuntimeExceptionFactory<>("数据量过多, 最多允许 %d 条数据", MessageException::new);
    public static final CustomRuntimeExceptionFactory<?> MSG_EMPTY_DATA = new CustomRuntimeExceptionFactory<>("还未填入数据, 请填入数据后返回", MessageException::new);
    public static final CustomRuntimeExceptionFactory<?> MSG_UNSUPPORTED_DATA = new CustomRuntimeExceptionFactory<>("不支持的数据格式, 请完整填入后返回", MessageException::new);
    public static final CustomRuntimeExceptionFactory<?> COUNTDOWN_LESS_NOW = new CustomRuntimeExceptionFactory<>("定时结束时间小于当前系统时间", MessageException::new);
    public static final CustomRuntimeExceptionFactory<?> COUNTDOWN_OVER_COUNT = new CustomRuntimeExceptionFactory<>("定时时长超过了范围", MessageException::new);

}
