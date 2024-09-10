redis**实例**

session共享问题

```
多台Tomcat并不共享session存储空间，当请求切换到不同tomcat服务时导致数据丢失问题
替换session应该满足：数据共享、内存存储、key-value结构
```

![image-20240909135943123](D:\a_briup_learn\redis\redis实例.assets\image-20240909135943123.png)

String结构适合比较小的，而推荐Map，它所占内存小是因为不用占空间存在{name:…},只需要存放0数据就好。

- **redis**适合需要会话管理和动态会话更新的应用，支持更灵活的会话操作和即时插销。

```
会话：是用户与服务器的交互中保持状态和数据。允许服务器记住用户在不同请求中的信息。
在 Web 开发中，会话机制通常涉及服务器生成一个唯一的会话 ID，并将其存储在客户端的 Cookie 中。服务器端则保存关于用户的状态信息，比如登录状态、购物车内容等。每次请求时，客户端会发送会话 ID，服务器根据这个 ID 识别用户并恢复相应的数据。

// Redis 数据库存储的会话数据示例
Key: "session:abcdef123456"
Value: {
  "user_id": "123456",
  "username": "JohnDoe",
  "roles": ["user"],
  "expires_at": "2024-09-09T12:00:00Z"
}
```

- **jwt**适合无状态的应用和服务，尤其在分布式系统中，可以轻松扩展且无服务器存储要求。

```
// Example JWT payload
{
  "sub": "1234567890", // Subject (user ID)
  "name": "John Doe",
  "iat": 1516239022, // Issued at
  "exp": 1516242822  // Expiration time
}
```

