package com.hmall.api.config;

import com.hmall.api.fallback.ItemClientFallbackFactory;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;



/**
 * @Author:kim
 * @Description:
 * @DateTime: 2024/9/14 6:34
 **/
public class DefaultFeignConfig {
    /**
     * 日志级别配置类
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 微服务之间的请求拦截器，想一想，用户id是从网关传入到微服务，而微服务之间还不能传递，设置一个拦截器是用户id在微服务间传递
     * 就比如：购物车扣减库存 是订单微服务传过来的userid，不设置拦截器扣减不会成功  在TradeApplication中加入
     * @EnableFeignClients(basePackages = "com.hmall.api.client",defaultConfiguration = DefaultFeignConfig.class)
     * @return
     */
    @Bean
    public RequestInterceptor userAgentInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userId = UserContext.getUser();
                if (userId!=null){
                    requestTemplate.header("user-info",userId.toString());
                }
            }
        };
    }

    //@FeignClient(value = "item-service",fallbackFactory = ItemClientFallbackFactory.class)
    @Bean
    public ItemClientFallbackFactory itemClientFallbackFactory(){
        return new ItemClientFallbackFactory();
    }
}
