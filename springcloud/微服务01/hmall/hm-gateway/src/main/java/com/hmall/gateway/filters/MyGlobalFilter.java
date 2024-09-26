package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/14 14:51
 **/
@Component
public class MyGlobalFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //TODO:模拟登陆校验逻辑
        //1.获取请求
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        //2.过滤器执行业务处理
        System.out.println("headers: " + headers);

        //3.放行
        return chain.filter(exchange);
    }

    //控制过滤器执行的位置，保证jwt校验在NettyRoutingFilter之前，
    // 所以设置Ordered接口 排序 这儿0<Integer.MAX_VALUE;所有在前面执行 也就是PRE
    @Override
    public int getOrder() {
        return 0;
    }

 }
