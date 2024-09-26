package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/12 6:23
 **/
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.230.132:6379").setPassword("429100");
        //创建RedissonClient对象
        return Redisson.create(config);
    }
    /*@Bean
    public RedissonClient redissonClient1(){
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.230.132:6380").setPassword("429100");
        //创建RedissonClient对象
        return Redisson.create(config);
    }
    @Bean
    public RedissonClient redissonClient2(){
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.230.132:6390").setPassword("429100");
        //创建RedissonClient对象
        return Redisson.create(config);
    }*/
}
