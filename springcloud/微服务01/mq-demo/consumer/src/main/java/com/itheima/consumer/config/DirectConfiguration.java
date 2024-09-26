package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:kim
 * @Description: 声明交换机和队列，将队列和交换机进行绑定的时候，会很麻烦不同的routingKey写不同的绑定，我们使用注解来简化
 * @DateTime: 2024/9/18 12:56
 **/
/*
@Configuration
public class DirectConfiguration {

    */
/**
     * 声明交换机
     * @return
     *//*

    @Bean
    public DirectExchange directExchange(){
        //两者方式
        //return new FanoutExchange("hmall.direct");
        return ExchangeBuilder.fanoutExchange("hmall.direct").build();
    }

    */
/**
     * 消息队列1
     * @return
     *//*

    @Bean
    public Queue directQueue1(){
        //return new Queue("direct.queue1");
        return QueueBuilder.durable("direct.queue1").build();  //durable 持久的 持久化到磁盘，不容易丢失
    }
    */
/**
     * 绑定关系 绑定队列和交换机
     *//*

    public Binding DirectQueue1BindingRed(Queue directQueue1, DirectExchange directExchange){
        return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
    }
    public Binding DirectQueue1BindingBlue(Queue directQueue1, DirectExchange directExchange){
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }

    */
/**
     * 消息队列1
     * @return
     *//*

    @Bean
    public Queue directQueue2(){
        //return new Queue("direct.queue2");
        return QueueBuilder.durable("direct.queue2").build();  //durable 持久的 持久化到磁盘，不容易丢失
    }
    */
/**
     * 绑定关系 绑定队列和交换机
     *//*

    public Binding DirectQueue2BindingRed(Queue directQueue2, DirectExchange directExchange){
        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
    }
    public Binding DirectQueue2BindingYellow(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
    }
}
*/
