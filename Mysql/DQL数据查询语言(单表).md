DQL数据查询语言(单表)

会基于原表数据查询出一个虚拟表

- SELECT:查询

![image-20240827161502354](C:\Users\ASUS\AppData\Roaming\Typora\typora-user-images\image-20240827161502354.png)

```sql
# * 通配符,必须放在第一位!!
SELECT *,'总部' AS etype FROM t_employee; 

SELECT ename  姓名 , salary  月薪 , salary * commission_pct 奖金 , salary +  salary * commission_pct 月总收入 FROM t_employee;
SELECT ename  姓名 , salary  月薪 , salary * IFNULL(commission_pct,0) 奖金 , salary +  salary * IFNULL(commission_pct,0) 月总收入 FROM t_employee;
# 因为,有些员工没有奖金, 奖金占比就是null, null 运算 任何值 = null
# ifnull(列,为null你给与的默认值) 0
```

