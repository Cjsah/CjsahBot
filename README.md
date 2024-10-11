### 黑盒语音bot框架 - Java

# 命令注册

```java
class TestCommands {
    /**
     * 使用 @Command 注解注册命令
     * 命令参数按参数顺序使用 <> 标记, 自动解析参数类型
     * 标记名称为实际参数名称, 方法参数名随意
     * 或使用 @CommandParam() 标记参数信息, 此时会忽略 <> 标记的参数名称
     */
    @Command("/cmd <p1> <p2>")
    public static void cmd1(@CommandParam("px") int param1, String param2, CommandSource<?> source) {
        long id = source.getId(); // 发送者的 id
        source.sendFeedback("msg"); // 发送回复消息, 支持 markdown
    }
}
```

