package com.kim.config;

import com.kim.interceptors.LoginProtectedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:kim
 * @Description: 拦截器配置类
 * @DateTime: 2024/9/6 22:29
 **/
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginProtectedInterceptor loginProtectedInterceptor;

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginProtectedInterceptor).addPathPatterns("/headline/**");
    }
}
