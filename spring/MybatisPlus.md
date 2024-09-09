# MybatisPlus(mp)

是Mybaits的优化版，魂斗罗一样相辅相成。

[MyBatis-Plus 🚀 为简化开发而生 (baomidou.com)](https://baomidou.com/)

**基本步骤**

1. 引入依赖，代替Mybaits
2. 认爸爸(extends BaseMapper<实体类>)
3. 在实体类上添加注解声明 表信息
4. 在application.yml中根据需要添加配置（可以覆盖原有的配置）

**常见注解**

![image-20240825195635498](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825195635498.png)

![image-20240825202222014](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825202222014.png)

用到自己在官网看

**条件构造器 QueryWrapper**

**Wrapper包装器   包装类Integer…**

![image-20240825204847352](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825204847352.png)

![](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825204608881.png

![image-20240825204821507](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825204821507.png)

```java 
/*
     * 查询出名字中带o的，存款大于等于1000的人的id,username,info,balance
     */
    @Test
    void testQueryMapper(){
        //1.构造查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id","username","info","balance")
                .like("username","o")
                .ge("balance",1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }
    /*
     *使用Lambda条件查询
     */
    @Test
    void testLambdaQueryWrapper(){
        //1.构造查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId,User::getUsername,User::getInfo,User::getBalance)
                .like(User::getUsername,"o")
                .ge(User::getBalance,1000);
        //2.查询
              List<User> users = userMapper.selectList(wrapper);
              users.forEach(System.out::println);
    }
    /*
     * 更新用户名为jack的用户余额为2000
     */
    @Test
    void testUpdateByQueryWrapper() {
        //1.要更新的数据
        User user = new User();
        user.setBalance(2000);
        //2.更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username","jack");
        //3.执行更新
        userMapper.update(user,wrapper);
    }
    /*
    更新id为1，2，4的用户的余额，扣200
    Update user
       set balance  = balance -200
       where id in (1,2,4)
     */
    void testUpdateWrapper(){
        List<Long> ids = List.of(1L,2L,4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id",ids);
        userMapper.update(null,wrapper);
    }
```

![image-20240825211847185](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825211847185.png)

**自定义**

![image-20240825212435092](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825212435092.png)

![image-20240825212558289](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825212558289.png)

where条件以外的部分，不是常规的值，只能去拼接  像案例中的-工资数，不是确定的200，而是用#{amount}，就需要自定义。

![image-20240825220434955](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825220434955.png)

![image-20240825221202834](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825221202834.png)

![image-20240825223001485](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240825223001485.png)

**Iservice的批量新增**

![image-20240826151907371](D:\a_briup_learn\spring\MybatisPlus.assets\image-20240826151907371.png)

**关于idea自动生成代码**

DB(database数据库)**静态工具**

添加与修改不需要.class通过反射获取属性..所以不需要传class类