package com.kim;

import com.kim.utils.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/5 15:00
 **/
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Autowired
    private JwtHelper jwtHelper;


    @Test
    public void jwtTest(){
        //生成 传入用户标识
        String token = jwtHelper.createToken(1L);
        System.out.println("令牌为："+token);

        //解析用户标识
        int userId = jwtHelper.getUserId(token).intValue();
        System.out.println("用户标识："+userId);

        //校验是否到期！
        boolean expiration = jwtHelper.isExpiration(token);
        System.out.println("是否到期："+expiration);
    }
}
