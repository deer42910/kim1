package com.hmall.common.interceptors;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:kim
 * @Description: 用户 登录  拦截器
 * @DateTime: 2024/9/15 5:45
 **/
//SpringMvc的拦截器生效要配置 写配置类MvcConfig
public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.获取登录用户信息  注意API不同：网关用的是WebFlax反应式编程，而这儿使用SpringMVC的servlet的
        String userInfo = request.getHeader("user-info");
        //2.判断是否获取了用户，若有，存入ThreadLocal
        if(StrUtil.isNotBlank(userInfo)){
            //不为空，保存到ThreadLocal
            UserContext.setUser(Long.valueOf(userInfo));
        }
        //3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清理用户
        UserContext.removeUser();
    }
}
