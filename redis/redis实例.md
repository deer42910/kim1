redis**实例**

session共享问题

```
多台Tomcat并不共享session存储空间，当请求切换到不同tomcat服务时导致数据丢失问题
替换session应该满足：数据共享、内存存储、key-value结构
```

![image-20240909135943123](D:\a_briup_learn\redis\redis实例.assets\image-20240909135943123.png)

String结构适合比较小的，而推荐Map，它所占内存小是因为不用占空间存在{name:…},只需要存放0数据就好。
