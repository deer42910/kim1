Map应用

**快速查找**

hashMap存储员工信息 ID为key 对象为value 根据ID查看详情信息

**数据去重**

唯一的key而不需要重复的value

hashSet确保每个元素的唯一

**频数计数**

统计单词的出现频次

getOrDefault(word,0)

**缓存实现**

**场景**: 需要临时存储计算结果以提高效率时。

**例子**: 使用 `LinkedHashMap` 实现一个最近最少使用（LRU）的缓存机制

```java
Map<Integer, String> cache = new LinkedHashMap<>(16, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
        return size() > 100; // 最大缓存大小为100
    }
};
```

**配置管理**

使用 `Properties` 类（`Map` 的子类）存储和加载应用程序的配置信息

```
Properties properties = new Properties();
properties.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
properties.setProperty("db.user", "admin");
properties.load(new FileInputStream("config.properties"));
```

**排序操作**

TreeMap按照自然顺序或自定义顺序

