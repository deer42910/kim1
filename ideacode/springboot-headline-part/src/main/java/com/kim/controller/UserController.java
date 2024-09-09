package com.kim.controller;

import com.kim.pojo.User;
import com.kim.service.UserService;
import com.kim.utils.JwtHelper;
import com.kim.utils.Result;
import com.kim.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/5 15:41
 **/
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;
    /**
     * 用户登录接口
     * @param user
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody User user) {
        Result result = userService.login(user);
        System.out.println("result="+result);
        return result;
    }


    /**
     * 根据token获取用户数据
     * @param token
     * @return
     */
    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader String token){
        Result result = userService.getUserInfo(token);
        return result;
    }

    /**
     * 注册
     * 查询是否存在该用户
     * @param username
     * @return
     */
    @PostMapping("checkUserName")
    public Result checkUserName(String username){
        Result result = userService.checkUserName(username);
        return result;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("regist")
    public Result regist(@RequestBody User user){
        Result result = userService.regist(user);
        return result;
    }

    /**
     * 登录验证与保护
     * @param token
     * @return
     */
    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        boolean expiration = jwtHelper.isExpiration(token);
        if(expiration){
            //为true就已经为过期了
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        return Result.ok(null);
    }
}
