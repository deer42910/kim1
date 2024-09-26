package com.itheima.publisher.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/20 21:31
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqConfig {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct  //初始化完成之后调用
    public void init(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.error("监听到了消息的return callback");
                log.debug("交换机：{}", returnedMessage.getExchange());
                log.debug("routingKey：{}", returnedMessage.getRoutingKey());
                log.debug("message：{}", returnedMessage.getMessage());
                log.debug("replyCode：{}", returnedMessage.getReplyCode());
                log.debug("replyTest：{}", returnedMessage.getReplyText());
            }
        });
    }

}
