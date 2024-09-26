package com.hmall.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author:kim
 * @Description: Auth与权限有关的 全局过滤器
 * @DateTime: 2024/9/14 16:59
 **/
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
//@EnableConfigurationProperties 注解用于启用 @ConfigurationProperties 注解的配置类。
//用于将外部配置（如 application.properties 或 application.yml 文件中的属性）绑定到一个 Java 对象
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    //注意有与权限相关的注解
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;

    //专门用来判断有通配符的 item/** ...  （在下面的isExclude中使用）
    //AntPathMatcher 是 Spring Framework 中的一个类，用于路径模式匹配。
    // 它主要用于处理和匹配 URL 模式，尤其是在 Spring MVC 和 Spring Security 中用于匹配请求路径与预定义模式之间的关系。
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    //ServerWebExchange上下文信息，获取请求...
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取用户信息 request
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是否需要做登录拦截
        if(isExclude(request.getPath().toString())){
            //放行
            return chain.filter(exchange);
        }
        //3.获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");//Authorization 头部字段(被规定的)用于传递认证信息，如 API 密钥、令牌或用户名密码等
        if(headers!=null&&!headers.isEmpty()){
            token = headers.get(0);
        }
        //4.校验并解析token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {//继承自 RuntimeException。它用于表示用户试图访问他们没有权限的资源时抛出的异常。
            //捕获异常，就是不抛出异常，也返回完结信息
            //拦截 设置响应码401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();  //拦截器终止，完结，，，它是Mono类型 直接返回
        }

        //TODO:5.若有效，传递用户信息 将用户信息从上文拿到后,这儿对用户信息进行修改 要保存在过滤器中的ThreadLocal中，要保存到common中UserInfoInterceptor
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate() //mutate就是对下游请求做更改
                .request(builder -> builder.header("user-info", userInfo)).build();
        //System.out.println("userId:"+userId);
        //6.放行
        return chain.filter(swe);
    }

    /**
     * 路径的校验
     * @param path
     * @return
     */
    private boolean isExclude(String path) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern,path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
