package com.hmall.gateway.filters;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author:kim
 * @Description: GatewayFilters比较复杂 实现的是抽象工厂方法 （过滤器工厂来创建过滤器对象）
 * @DateTime: 2024/9/14 15:09
 **/
@Component
public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<PrintAnyGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        //匿名内部类不能设置顺序，所以提供了一个修饰类 OrderedGatewayFilter 是GatewayFilter的子类
        //包含两个参数
        //- GatewayFilter：过滤器
        //- int order值：值越小，过滤器执行优先级越高
        return new OrderedGatewayFilter(
                new GatewayFilter() {  //匿名内部类
                    @Override
                    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                        //获取config值
                        String a = config.getA();
                        String b = config.getB();
                        String c = config.getC();
                        //编写过滤器逻辑
                        System.out.println("a="+a);
                        System.out.println("b="+b);
                        System.out.println("c="+c);
                        //放行
                        return chain.filter(exchange);
                    }
                },100);
    }

    //自定义配置属性，成员变量名称很重要
    @Data
    static class Config{
        private String a;
        private String b;
        private String c;
    }

    //将变量名称依次返回，顺序很重要，将来读取参数时需要按照顺序获取
    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("a", "b", "c");
    }
    //返回当前配置类的类型，也就是内部的Config
    @Override
    public Class<Config> getConfigClass(){
        return Config.class;
    }


    /*spring:
  cloud:
    gateway:
      default-filters:
            - PrintAny=1,2,3
  # 注意，这里多个参数以","隔开，将来会按照shortcutFieldOrder()方法返回的参数顺序依次复制
     */
    /*另一种*/
/*spring:
  cloud:
    gateway:
      default-filters:
            - name: PrintAny
              args: # 手动指定参数名，无需按照参数顺序
                a: 1
                b: 2
                c: 3
                */

}
