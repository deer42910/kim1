多表查询DQL

**垂直合并语法**：浏览网页（将多个表的结果汇总，不要求主外键）

union # 合并记录同时去掉重复数据

union all # 合并记录,且不去掉重复数据

> 想想就是：服装、鞋包、食品…要合并到一张统一的表

```
# 数据准备
CREATE TABLE a(
   aid INT,
   aname VARCHAR(10)
);

CREATE TABLE b(
   bid INT,
   bname VARCHAR(10)
);


INSERT INTO a VALUES(1,'aaaa'),(2,'bbbb'),(3,'cccc');
INSERT INTO b VALUES(4,'aaaa'),(2,'bbbb'),(3,'cccc');

#去重复合并
SELECT aid,aname FROM a
UNION 
SELECT bid,bname FROM b;

#不去重复合并
SELECT aid,aname FROM a
UNION ALL
SELECT bid,bname FROM b;
```

**水平合并语法**：数据库中的表（连接查询,水平整理，要求主外键）