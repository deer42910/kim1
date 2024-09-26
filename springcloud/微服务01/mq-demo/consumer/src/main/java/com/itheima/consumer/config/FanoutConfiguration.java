package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 12:56
 **/
@Configuration
public class FanoutConfiguration {

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        //两者方式
        //return new FanoutExchange("hmall.fanout");
        return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
    }

    /**
     * 消息队列1
     * @return
     */
    @Bean
    public Queue fanoutQueue1(){
        //return new Queue("fanout.queue1");
        return QueueBuilder.durable("fanout.queue1").build();  //durable 持久的 持久化到磁盘，不容易丢失
    }
    /**
     * 绑定关系 绑定队列和交换机
     */
    public Binding FanoutQueue1Binding(Queue fanoutQueue1, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 消息队列1
     * @return
     */
    @Bean
    public Queue fanoutQueue2(){
        //return new Queue("fanout.queue2");
        return QueueBuilder.durable("fanout.queue2").build();  //durable 持久的 持久化到磁盘，不容易丢失
    }
    /**
     * 绑定关系 绑定队列和交换机
     */
    public Binding FanoutQueue2Binding(Queue fanoutQueue2, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
