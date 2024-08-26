# MyBatis

```
#驱动类名称
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#数据库连接的url
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis
#连接数据库的用户名
spring.datasource.username=root
#连接数据库的密码
spring.datasource.password=root

#指定mybatis输出日志的位置, 输出控制台
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

# 在application.properties中添加：
mybatis.configuration.map-underscore-to-camel-case=true
```

```
JDBC Connection [HikariProxyConnection@1597736125 wrapping com.mysql.cj.jdbc.ConnectionImpl@34aa8b61] will not be managed by Spring
==>  Preparing: delete from emp where id = ?
==> Parameters: 16(Integer)
<==    Updates: 1
```

预编译sql

> 1. 性能更高，编译一次之后会将编译后的SQL语句缓存起来，后面再次执行这条语句时，不会再次编译。（只是输入的参数不同）
> 2. 更安全（防止sql注入）：将敏感字进行转义，保障SQL的安全性。

 **参数占位符**

在Mybatis中提供的参数占位符有两种：${...} 、#{...}

- #{...}
  - 执行SQL时，会将#{…}替换为?，生成预编译SQL，会自动设置参数值
  - 使用时机：参数传递，都使用#{…}

- ${...}
  - 拼接SQL。直接将参数拼接在SQL语句中，存在SQL注入问题
  - 使用时机：如果对表名、列表进行动态设置时使用

**tinyint**

存储整数数据的SQL数据类型：

**特点**

1. **存储大小**：
   - `TINYINT` 占用的存储空间较小，通常为 1 字节（8 位）。
2. **值范围**：
   - 无符号（Unsigned）
     - 0 到 255（适用于不带符号的 `TINYINT`）。
   - 有符号（Signed）
     - -128 到 127（适用于带符号的 `TINYINT`）。

**插入数据之后返回所插入行的主键值**

需要在Mapper接口中的方法上添加一个Options注解，并在注解中指定属性useGeneratedKeys=true和keyProperty="实体类属性名"

```java
 //会自动将生成的主键值，赋值给emp对象的id属性
    @Options(useGeneratedKeys = true,keyProperty = "id")
```

