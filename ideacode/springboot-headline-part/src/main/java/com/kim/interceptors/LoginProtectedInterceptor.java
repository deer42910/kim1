package com.kim.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.utils.JwtHelper;
import com.kim.utils.Result;
import com.kim.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author:kim
 * @Description: 登录检查拦截器，检查请求头是否包含有效token
 * @DateTime: 2024/9/6 22:20
 *  有 有效 放行
 *  没有，无效，504
 **/
@Component
public class LoginProtectedInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("token");
        //检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);
        //有效放行
        if(!expiration){
            //没过期
            return true;
        }
        //无效504  405状态的json
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);

        //写成字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);//字符输出流打印


        return false;
    }
}
