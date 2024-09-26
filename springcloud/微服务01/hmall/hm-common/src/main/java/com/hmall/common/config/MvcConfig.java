package com.hmall.common.config;

import com.hmall.common.interceptors.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/15 5:57
 **/
@Configuration //配置类：要想生效，需要被spring扫描包扫描到 微服务不同 包不同，在spring.factories文件中，写入，会使项目启动时自动加载配置类
@ConditionalOnClass(DispatcherServlet.class)   //springBoot自动装配的条件注解 让网关不生效，mvc中的都生效
//WebMvcConfigurer文件找不到
//应网关service-Gateway的pom文件中也common配置，会被自动加载，但是一个是WebFlax非阻塞式的响应编程，一个是mvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor());//.addPathPatterns("/**");设置拦截所有的，直接默认
    }
}
