package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 12:56
 **/
@Configuration
public class NormalConfiguration {

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public DirectExchange normalExchange(){
        return new DirectExchange("normal.direct");
    }

    /**
     * 消息队列:
     * @return
     */
    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable("normal.queue").deadLetterExchange("dlx.direct")
                .build();
    }
    /**
     * 绑定关系 绑定队列和交换机
     */
    public Binding normalQueueBinding(Queue normalQueue, DirectExchange directExchange){
        return BindingBuilder.bind(normalQueue).to(directExchange).with("hi");
    }


}
