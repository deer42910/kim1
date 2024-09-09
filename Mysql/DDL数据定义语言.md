## DDL数据定义语言

命名规则

阿里巴巴规范手册 _db数据库  _table表名  列名

**创建库**

```
CREATE DATADASE IF NOT EXISTS 数据库名;

CREATE DATADASE 数据库名 CHARACTER SET 字符集;
CREATE DATADASE 数据库名 COLLATE 排序规则;
CREATE DATADASE 数据库名 CHARACTER SET 字符集 COLLATE 排序规则;
```

![image-20240827103419192](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827103419192.png)

```
#查看默认字符集和排序方式
SHOW VARIABLES LIKE 'character_set_database';
SHOW VARIABLES LIKE 'collation_database';
```

**查看与使用库**

```sql
＃查看当前所有库
SHOW DATABASES;
#查看当前使用库
SELECT DATABASE();
#查看指定库下的所有表
SHOW TABLES FROM 数据库名;
#查看创建库的信息
SHOW CREATE DATABASE 数据库名;
#切换数据库
USE 数据库名;
```

**#注意:要操作表格和数据之前必须先说明是哪个数据库进行操作，先use库**

**修改库**

```
#修改库编码字符集
ALTER DATABASE 数据库名 CHARACTER SET 字符集 COLLATE 排序方式;
```

ＤＡＴＡＢＡＳＥ不能改名。一些可视化工具可以改，他是创建新库，把所有表复杂到新库、再删除旧库。

```
＃判断并删除库
DROP DATABASE IF EXISTS 数据库名；
```

**表管理**

![image-20240827105834180](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827105834180.png)

![image-20240827105842231](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827105842231.png)　

**数据类型**

- 整数

  | 类型      | 存储 | 最小值有符号     | 最小值无符号 | 最大值有符号       | 最大值无符号 |
  | --------- | ---- | ---------------- | ------------ | ------------------ | ------------ |
  | TINYINT   | １   | －128            | 0            | 127                | 255          |
  | SMALLINT  | ２   | －３２７６８     | ０           | ３２７６８         |              |
  | MEDIUMINT | ３   | －８３８８６０８ | ０           | ８３８８６０８     |              |
  | INT       | ４   | －214783648      | ０           | ２１４７８３６４８ |              |
  | BIGINT    | ５   | －２＾６３       | ０           | ２＾６３           | ２＾６４－１ |

  无符号＝＝无符号  unsigned修饰符 值从0开始

  ```
  #无符号值从0开始 tinyint型
  stu_age tinyint unsigned comment ‘年龄字段’,
  #注意紧跟在 类型 后
  ```

- 浮点数

  | 类型        | 存储（字节） | M(小数+整数位置) | D小数位置 |
  | ----------- | ------------ | ---------------- | --------- |
  | FLOAT(M,D)  | 4            | M最大为24        | D最大为8  |
  | DOUBLE(M,D) | 8            | M最大为53        | D最大为30 |

  注意：mySQL8.0.17后推荐使用以上两种，后面可能会删除。支持unsigned修饰，只保留正数范围不会迁移到正值

  ```
  stu height float(4,1) unsigned comment'身高，保留一位小数，多余会四舍五入',
  ```

- 定点数

  | 类型         | 存储（字节） | M(小数+整数位置) | D小数位置 |
  | ------------ | ------------ | ---------------- | --------- |
  | DECLMAL(M,D) | 动态计算     | M最大为65        | D最大为30 |

  存储空间是可变的，存储大小受declmal数据类型定义时指定的精度和规模影响。若D为0，不包含小数点或小数部分。

  ```
  emp_salary DECLMAL(8,1) comment'工资，保留一位小数，多位会四舍五入',
  ```

  **精度要求不高,例如:身高,体重 float / double** 
  **精度要求特别高,钱 工资,价格 decimal** 

- 字符串

- | 字符串（文本） | 特点     | 长度 | 长度范围(字符)             | 存储空间               |
  | -------------- | -------- | ---- | -------------------------- | ---------------------- |
  | CHAR(M)        | 固定长度 | M    | 0<=M<=255                  | M*4个字节(utf8mb4)     |
  | VARCHAR(M)     | 可变长度 | M    | Mysql一行数据最多65535字节 | (M*4+1)个字节(utf8mb4) |

  注意：char(1)默认1，自己设置；实际写入比声明长度小，右侧填充空格；MySQL检查char类型的数据时，会除尾部的空格。

  ​           varchar(M)必须指定长度 MySQL4.0下，varchar(20)20字节，若放UTF-8汉字，只能放6个(每个汉字3字节)；5.0以上，指的是字符。MySQL检查varchar类型的数据时，会保留尾部的空格。

  ![image-20240827115000080](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827115000080.png)

  ![image-20240827115308634](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827115308634.png)

- 文本类型

  | 文本字符串类型 | 特点             | 长度 | 长度范围(字符)   | 存储空间  |
  | -------------- | ---------------- | ---- | ---------------- | --------- |
  | TEXT           | 文本、可变长度   | L    | 0<=x<=65535      | L+2个字节 |
  | TINYTEXT       | 小文本、可变长度 | L    | 0<=x<=255        | L+2个字节 |
  | LONGTEXT       | 大文本、可变长度 | L    | 0<=x<=4294967295 | L+2个字节 |
  
  ```
  tx TEXT
  ```
  
  > **开发经验：**
  >
  > -   短文本，固定长度使用char     例如：性别，手机号
  >
  > -   短文本，非固定长度使用varchar 例如：姓名，地址
  >
  > -   大文本，建议存储到文本文件，使用varchar记录文件地址，不使用TEXT，直接存储大文本，他性能非常较差！
  
- 时间类型

  | 类型      | 名称     | 字节 | 日期格式                          | 小值                | 最大值              |
  | --------- | -------- | ---- | --------------------------------- | ------------------- | ------------------- |
  | YEAR      | 年       | 1    | YYYY或YY(00-69)-2000 (70-99)-1900 | 1901                | 2155                |
  | TIME      | 时间     | 3    | HH:MM:SS                          | -838：59：59        | 838：59：59         |
  | DATE      | 日期     | 3    | YYYY-MM-DD                        | 1000-01-01          | 9999-12-03          |
  | DATETIME  | 日期时间 | 8    | YYYY-MM-DD HH:MM:SS               | 1000-01-01 00:00:00 | 9999-12-31 23:59:59 |
  | TIMESTAMP | 日期时间 | 4    | YYYY-MM-DD HH:MM:SS               | 1970-01-01 00:00:00 | 2038-01-19 03:14:07 |

  ```sql
  # 方式1： 插入默认当前时间和修改自动更新当前时间
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  dt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  # 方式2： 插入默认当前时间
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
  dt DATETIME DEFAULT CURRENT_TIMESTAMP
  ```

**确定数据值范围，选择符合范围且存储空间占有最小类型**

**不确定数据值范围，选择选择范围较大类型，避免超值超范围异常**

**修改表**

```sql
#修改表，添加一列[可以指定x字段前或者后]
ALTER TABLE 表名 ADD 字段名 字段类型 [FIRST|AFTER字段名];
#修改表，修改列名
ALTER TABLE 表名 CHANGE 原字段名 字段名 新字段类型 [FIRST|AFTER字段名];
#修改表，修改列类型
ALTER TABLE 表名 MODIFY 字段名 新字段类型 [FIRST|AFTER字段名];
#删除表，删除一列
ALTER TABLE 表名 DROP 字段名;

#修改表名
ALTER TABLE 表名 RENAME [TO] 新表名;
```

**删除表**

注意：删除表和清空表数据命令都是无法回滚的！执行之前请三思

```sql
#删除表
DROP TABLE [IF EXISTS] 数据表1[,数据表2，数据表3...,数据表n];
#清空表数据
TRUNCATE TABLE 表名;
```

```sql
创建一个学生表(student)来存储借书的学员信息，其中应包含学生姓名、年龄、身高、生日以及注册时间和更新时间等属性。
  
  student -> 字符集 -> 默认
    姓名 -> stu_name  varchar(20)
    性别 -> stu_sex  char
    年龄 -> stu_age   tinyint unsigned
    身高 -> stu_height double(4,1)
    生日 -> stu_birthday date 
    注册 -> stu_regtime datetime default current_timestamp 
    更新 -> stu_uptime  datetime default current_timestamp on update current_timestamp 
  
*/

USE book_libs;
CREATE TABLE if NOt EXISTS student (
  stu_name VARCHAR(10) NOT NUll COMMENT '姓名',
	stu_sex CHAR COMMENT '学生性别，默认一个字符',
	stu_age TINYINT UNSIGNED COMMENT '年龄，无符号', 
	stu_heigh FLOAT(4,1) COMMENT '身高',
	stu_birthday DATE COMMENT '生日',
	stu_regtime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
	stu_uptime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'
	);
```

**注意：**use 数据库;一定不要忘

​           自己写comment的时候仔细，不要弄成commit

​           时()而不是{}  一定要英文
