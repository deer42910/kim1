package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 10:52
 **/
@Slf4j
@SpringBootTest
class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue(){
        //1.队列名
        String queueName = "simple.queue";
        //2.消息
        String message="hello spring amqp!";
        //3.发送消息
        rabbitTemplate.convertAndSend(queueName,message);
    }

    @Test
    public void testWorkQueue1() throws InterruptedException {
        //1.队列名
        String queueName = "work.queue";
        //2.消息
        String message="hello, message_";
        for (int i = 0; i < 50; i++) {
            //3.发送消息  每20毫秒发送一次，相当于每秒发送50条
            rabbitTemplate.convertAndSend(queueName,message+i);
            Thread.sleep(20);
        }
    }
    @Test
    public void testFanoutQueue(){
        //1.交换机名
        String exchangeName = "hmall.fanout";
        //2.消息
        String message="hello exchange 的 queue";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,null,message); //三个参数就是交换机
    }

    @Test
    public void testDirectQueue(){
        //1.交换机名
        String exchangeName = "hmall.direct";
        //2.消息
        String message="hello exchange 的 direct.queue 震惊！！！！";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,"blue",message); //三个参数就是交换机
    }

    @Test
    public void testTopicQueue(){
        //1.交换机名
        String exchangeName = "hmall.topic";
        //2.消息
        String message="hello exchange 的 topic.queue 震惊！！！！";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,"china.news",message); //三个参数就是交换机
    }

    @Test
    public void testSendObject(){
        //1.准备消息
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("name","jack");
        msg.put("age",21);
        //3.发送消息
        rabbitTemplate.convertAndSend("object.queue",msg);
    }

    @Test
    public void testConfirmCallback() throws InterruptedException {
        //0.创建correlationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString()); //多个消息的id要确保唯一性
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Spring AMQP 处理确认结果异常",ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                //判断是否成功
                if(result.isAck()){
                    log.debug("收到confirmCallback ack,消息发送成功！");
                }else {
                    log.debug("收到confirmCallback Nack,消息发送失败,失败原因:{}",result.getReason());

                }
            }
        });
        //1.交换机
        String exchangeName = "hmall.direct";
        //2.消息
        String message = "蓝色：通知：女尸是充气的";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,"blue",message,cd);

        Thread.sleep(2000);//充分的时间进行回调
    }

    @Test
    void testSendMessage(){
        //1.自定义构造消息
        MessageBuilder.withBody("hello,SpringAMQP".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        //2.发送消息
        for (int i = 0; i < 100000; i++) {
            rabbitTemplate.convertAndSend("simple.queue","hello,springAMQP");
        }
    }

    @Test
    void testSendLazyMessage(){
        //1.自定义构造消息
        MessageBuilder.withBody("hello,SpringAMQP".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();
        //2.发送消息
        for (int i = 0; i < 100000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue","hello,springAMQP");
        }
    }

    //死信延迟
    void testSendDelayMessage(){
        rabbitTemplate.convertAndSend("normal.direct","hi","hello",message ->{
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
    }
}