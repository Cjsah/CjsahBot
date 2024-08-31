### 目前能想到的权限配置方式

```json5
{
  "global": [
    {
      "type": "user_set",
      "permission": "owner",
      "list": [12345678, 87654321]
    }
  ],
  "plugins": [
    {
      "id": "...",
      "permissions": [
        {
          "type": "white_group",
          "list": [12345678, 87654321]
        },
        {
          "type": "white_user",
          "list": [12345678, 87654321]
        },
        {
          "type": "black_group",
          "list": [12345678, 87654321]
        },
        {
          "type": "black_user",
          "list": [12345678, 87654321]
        },
        {
          "type": "user_set",
          "permission": "owner",
          "list": [12345678, 87654321]
        }
      ]
    }
  ]
}
```