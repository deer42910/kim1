```java
package com.kim.utils;
/**
 * @Author:kim
 * @Description: 统一返回结果状态信息类
 * @DateTime: 2024/9/4 23:47
 **/
public enum ResultCodeEnum {

    SUCCESS(200,"success"),
    USERNAME_ERROR(501,"usernameError"),
    PASSWORD_ERROR(503,"passwordError"),
    NOTLOGIN(504,"notLogin"),
    USERNAME_USED(505,"userNameUsed");

    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
```

```
Java枚举不仅仅是固定的常量集合，实际上是一个类，它可以有字段、方法、构造函数和其他类成员变量
  1.定义常量：每个常量都有两个属性
    SUCCESS(200,"success"),
    USERNAME_ERROR(501,"usernameError"),
    PASSWORD_ERROR(503,"passwordError"),
    NOTLOGIN(504,"notLogin"),
    USERNAME_USED(505,"userNameUsed");
  2.私有构造函数
    初始化枚举常量的两个属性
    是私有的原因是：枚举常量是固定的，枚举的实例是由Java在类加载时创建的，外部无法直接创建或修改这些实例
  3.属性和方法
    code和message是枚举类的两个实例变量
    getcode()和getMessage()用于访问这些变量的公共方法
```

