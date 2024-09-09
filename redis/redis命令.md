redis命令

**通用命令**

- KEYS：查看符合模板的所有key
- DEL：删除一个指定的key
- EXISTS：判断key是否存在
- EXPIRE：给一个key设置有效期，有效期到期时该key会被自动删除expire ttl
- TTL：查看一个KEY的剩余有效期

通过help [command] 可以查看一个命令的具体用法，例如：

```
192.168.230.131:6379> help keys

  KEYS pattern
  summary: Find all keys matching the given pattern
  since: 1.0.0
  group: generic
```

**String类型常见命令**

- String类型，也就是字符串类型，是Redis中最简单的存储类型。

  其value是字符串，不过根据字符串的格式不同，又可以分为3类：

  - string：普通字符串
  - int：整数类型，可以做自增、自减操作
  - float：浮点类型，可以做自增、自减操作

  不管是哪种格式，底层都是字节数组形式存储，只不过是编码方式不同。字符串类型的最大空间不能超过512m.

  ![](D:\a_briup_learn\redis\redis命令.assets\VZqpv73.png)

  String的常见命令有：

  - SET：添加或者修改已经存在的一个String类型的键值对
  - GET：根据key获取String类型的value
  - MSET：批量添加多个String类型的键值对
  - MGET：根据多个key获取多个String类型的value  m
  - INCR：让一个整型的key自增1  
  - INCRBY:让一个整型的key自增并指定步长，例如：incrby num 2 让num值自增2
  - INCRBYFLOAT：让一个浮点类型的数字自增并指定步长
  - SETNX：**添加**一个String类型的键值对，前提是这个**key不存在**，否则不执行 【是set 与 nx】的组合
  - SETEX：添加一个String类型的键值对，并且指定有效期  【set与expire 设置一个有效期，有效期没了就不执行了】【ttl查看倒计时】

  

