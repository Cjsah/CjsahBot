### 黑盒语音bot框架 - Java

# 插件开发

> Maven仓库 https://server.cjsah.net:1002/maven

> 依赖 `net.cjsah.bot:HeyBoxBotConsole:1.0.0`

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

# 权限修改

- 权限列表
  ```json5
  [
    {
      "type": "white_room", // 房间白名单
      "list": ["12345678", "87654321"]
    },
    {
      "type": "white_channel",  // 频道白名单
      "list": ["12345678", "87654321"]
    },
    {
      "type": "white_user",  // 用户白名单
      "list": [12345678, 87654321]
    },
    {
      "type": "black_room",  // 房间黑名单
      "list": ["12345678", "87654321"]
    },
    {
      "type": "black_channel",  // 频道黑名单
      "list": ["12345678", "87654321"]
    },
    {
      "type": "black_user",  // 用户黑名单
      "list": [12345678, 87654321]
    },
    {
      "type": "user_set",  // 用户组设置
      "permission": "owner", // 用户组
      "list": [12345678, 87654321]
     }
  ]
  ```
- 权限配置文件
  ```json5
  {
    // 设置全局的用户权限和黑白名单, 内容见上方权限列表
    "global": [],
    //设置插件的用户权限和黑白名单
    "plugins": [
      {
        // 插件 id
        "id": "...",
        // 设置插件权限, 内容见上方权限列表
        "permissions": []
      }
    ],
    //设置命令的用户权限和黑白名单
    "commands": [ 
      {
        // 命令名称
        "id": "/cmd",
        // 设置命令权限, 内容见上方权限列表
        "permissions": []
      },
    ]
  }
  ```

