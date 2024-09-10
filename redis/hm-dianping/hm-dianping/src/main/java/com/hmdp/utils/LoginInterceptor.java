package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author:kim
 * @Description: 登录拦截器
 * @DateTime: 2024/8/29 19:18
 **/
public class LoginInterceptor implements HandlerInterceptor {

    /*因为LoginInterceptor是手动创建的，使用构造器获得对象
    * 拦截器加个@Component注解也可以*/
    private StringRedisTemplate stringRedisTemplate;
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取session
        //HttpSession session = request.getSession();
        //1.获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)){
            //不存在，拦截，返回状态码
            response.setStatus(401);
            return false;
        }
        //2.基于token获取redis用户
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        //2.获取session中的用户
        //Object user = session.getAttribute("user");
        //3.判断用户是否存在
        /*if (user == null) {
            //4.不存在，拦截，返回401状态码
            response.setStatus(401);
            return false;
        }*/
        if(userMap.isEmpty()){
            //不存在，拦截，返回状态码
            response.setStatus(401);
            return false;
        }
        //5.TODO:将查询到的Hash数据转为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        //5.存在，保护用户信息到ThreadLocal
        //UserHolder.saveUser((UserDTO) user);
        //6.放行
        //TODO:刷新token有效期
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //移除用户
        UserHolder.removeUser();
    }
}
