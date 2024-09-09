package com.kim.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.pojo.User;
import com.kim.service.UserService;
import com.kim.mapper.UserMapper;
import com.kim.utils.JwtHelper;
import com.kim.utils.MD5Util;
import com.kim.utils.Result;
import com.kim.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-09-04 23:55:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 登录业务实现
     * 1.根据账号，查询用户对象 - loginuser
     * 2.若用户对象为空，查询失败，账号错误 501
     * 3.对比，密码石板返回503
     * 4.根据用户id生成一个token
     * 5.将token返回
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
        //1.根据账号，查询用户对象
        //根据账户查询
        //MyBatis-Plus提供一个工具类，用于生成SQL查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        //eq方法指定等于条件。
        //User::getUsername是一个方法引用，User类中的getUsername方法，用于获取数据库中的username列。
        //user.getUsername()是要匹配的值，传入user对象中的username属性值
        //就是查找username列值等于user.getUsername()的记录
        queryWrapper.eq(User::getUsername, user.getUsername());
        //userMapper 是一个 MyBatis-Plus 的 Mapper 接口的实例
        //执行数据库操作
        //selectOne 方法根据 queryWrapper 中的条件查询出一条记录。如果查询到匹配的记录，它将返回一个 User 对象，表示登录用户。如果没有找到匹配的记录，则返回 null。
        User loginUser = userMapper.selectOne(queryWrapper);

        //账号判断
        if(loginUser == null){
            //账号错误
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //账号密码
        if(!StringUtils.isEmpty(user.getUserPwd())
                &&loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd()))){
            //账号密码正确
            //根据用户唯一标识生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            Map data = new HashMap();
            data.put("token", token);
            return Result.ok(data);
        }

        //密码错误
        return Result.build(null, ResultCodeEnum.USERNAME_ERROR);

    }

    /**
     * 1.获取token，解析token对应的userId
     * 2.根据userId,查询用户顺序
     * 3.将用户数据的密码置空，并且把用户数据封装到结果中key=loginUser
     * 4.失败返回504（本次先写当前业务，后期提取到拦截器和全局异常处理器）
     * @param token
     * @return
     */
    @Override
    public Result getUserInfo(String token) {
        //判断token是否有效
        if(jwtHelper.isExpiration(token)){
            //true过期，直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询该用户数据
        User user = userMapper.selectById(userId);

        if (user!=null){
            //将密码置空
            user.setUserPwd(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }
        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }

    /**
     * 检查用户是否以注册
     * 1.获取账号数据
     * 2.根据账号进行数据库查询
     * 3.结果封装
     * @param username
     * @return
     */
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(queryWrapper);

        if (user!=null)
            return Result.build(null,ResultCodeEnum.USERNAME_USED);

        return Result.ok(null);
    }

    /**
     * 用户注册
     * 1。将密码加密
     * 2.将数据插入
     * 3.判断结果，成 返回200 失败 505
     * @param user
     * @return
     */
    @Override
    public Result regist(User user) {
        //客户端将新用户信息发送给客户端，服务端将新用户存入数据库
        //存入之前做用户是否被占用校验
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);

        if(count>0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        int rows = userMapper.insert(user);
        System.out.println("rows =" +rows);
        return Result.ok(null);
    }
}




