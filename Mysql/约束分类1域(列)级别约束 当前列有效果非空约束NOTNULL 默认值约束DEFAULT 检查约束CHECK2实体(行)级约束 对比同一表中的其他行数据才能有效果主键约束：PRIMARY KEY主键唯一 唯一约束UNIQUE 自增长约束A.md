```sql
约束分类
1域(列)级别约束 当前列有效果
非空约束NOTNULL 默认值约束DEFAULT 检查约束CHECK
2实体(行)级约束 对比同一表中的其他行数据才能有效果
主键约束：PRIMARY KEY主键唯一 唯一约束UNIQUE 自增长约束AUTO_INCREMENT
3.引用(多表)级约束
参考(外键)约束：FOREIGN KEY

-----------------------------------------------------------
#非空约束  NOT NULL 添加到列后面
所有类型列默认都可以为空，包括数字类型
只能添加在列上（创建表时）
空字符串、0都不是null，只有null才是空。
#创建表时添加
CREATE TABLE emp1(
    e_name VARCHAR(20) NOT NULL,
		e_age INT
)
#查看列信息
DESC emp1;
#创建表以后修改
ALTER TABLE emp1 MODIFY e_age INT Not NULL;
#删除非空约束
ALTER TABLE emp1 MODIFY e_age INT NULL;
ALTER TABLE emp1 MODIFY e_name VARCHAR(20);
-----------------------------------
#默认值约束  DEFAULT 默认值 添加到列后面
不能添加到主键或者唯一上，其他列都可以  
#建表时添加
CREATE TABLE emp2(
   NAME VARCHAR(20) DEFAULT '二狗子',
	 age INT NOT NULL
)
DESC emp2;
#建表后修改
ALTER TABLE emp2 MODIFY age INT DEFAULT(18) NOT NULL;
#-------后面可以插入null，注意设置了非空就会插入失败
#删除默认值约束
ALTER TABLE emp2 MODIFY NAME VARCHAR(20);
------------------------------------
#检查约束  check(检查表达式)
可以实现任何校验结果
不属于一个列，属于表级别的约束
8+才支持
check(表达式)，可以自定义表达式，变成任何约束！
不建议使用，进行数据检查，建议程序级限制，不然数据库压力太大了
#建表时添加
CREATE TABLE emp3(
  gender CHAR,
	CHECK(gender IN ('男','女')),
	age INT
)
#建表后修改
ALTER TABLE emp3 ADD CONSTRAINT age_ck CHECK(age>=18);
#删除
ALTER TABLE emp3 DROP CONSTRAINT 约束名;#约束名使用下面查看约束语句得到CONSTRAINT_NAME
#关于查看约束 数据库中专门有一张表information_schema.tables_constraints
SELECT *
FROM information_schema.tables_constraints
WHERE TABLE_SCHEMA = '库名'
AND TABLE_NAME = 'emp3';
-------------------------------------------------------------------------------------
#唯一约束  UNIQUE 限制列可以为空，但有值，必需唯一
一个表可以有多个唯一约束
唯一约束允许列值为空 就像手机好，可以为空，但有值必需唯一
唯一约束的名字默认是列名
#添加时添加
CREATE TABLE emp4(
  NAME VARCHAR(20),
	phone VARCHAR(11) UNIQUE
);
CREATE TABLE emp5(
  NAME VARCHAR(20),
	phone VARCHAR(11) UNIQUE KEY
);
CREATE TABLE emp6(
  NAME VARCHAR(20),
	phone VARCHAR(11),
	UNIQUE KEY(phone)
);
CREATE TABLE emp7(
  classes INT,
  num INT,
  NAME VARCHAR(20),
	phone VARCHAR(11),
	UNIQUE KEY(classes,num)  #组合索引
);
#查看唯一约束
SELECT *
FROM information_schema.tables_constraints
WHERE TABLE_SCHEMA = '库名'
AND TABLE_NAME = 'emp7';
#建表后修改		
ALTER TABLE emp7 ADD CONSTRAINT emp7_name UNIQUE KEY(NAME);
#删除
ALTER TABLE emp7 DROP CONSTRAINT emp7_name;
```

