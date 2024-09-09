package com.kim.service;

import com.kim.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kim.utils.Result;

/**
* @author ASUS
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-09-04 23:55:53
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
