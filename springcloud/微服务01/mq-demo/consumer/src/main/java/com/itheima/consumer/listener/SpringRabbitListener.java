package com.itheima.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 11:17
 **/
@Slf4j
@Component
public class SpringRabbitListener {

    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(Message msg)throws InterruptedException{
        //这儿的参数，是用什么发送的就用什么接，这儿是String
        log.info("spring消费者接收到消息：【{}】",msg.getMessageProperties().getMessageId());
        log.info("spring消费者接收到消息：【{}】",msg.getBody());
        //消费者确认机制 测试
        //throw new RuntimeException("我是故意的!重试");
        //throw new MessageConversionException("消息转换异常，reject拒绝");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg)throws InterruptedException{
        System.out.println("消费者1接收到消息："+msg+", "+ LocalDateTime.now());
        Thread.sleep(20);
    }
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg)throws InterruptedException{
        System.err.println("消费者2接收到消息："+msg+", "+ LocalDateTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg)throws InterruptedException{
        System.out.println("消费者1监听到消息："+msg);
    }
    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg)throws InterruptedException{
        System.err.println("消费者2监听到消息："+msg);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1",durable = "true"),
            exchange = @Exchange(name = "hmall.direct",type = ExchangeTypes.DIRECT),//默认就是direct
            key = {"red","blue"}
    ))
    public void listenDirectQueue1(String msg)throws InterruptedException{
        System.out.println("消费者1监听到direct.queue1消息："+msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2",durable = "true"),
            exchange = @Exchange(name = "hmall.direct",type = ExchangeTypes.DIRECT),//默认就是direct
            key = {"red","yellow"}
    ))
    public void listenDirectQueue2(String msg)throws InterruptedException{
        System.err.println("消费者2监听到direct.queue2消息："+msg);
    }


    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg)throws InterruptedException{
        System.out.println("消费者1接收到的topic.queue1消息："+msg);
    }
    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg)throws InterruptedException{
        System.err.println("消费者2接收到的topic.queue2消息："+msg);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> msg){
        System.err.println("消费者接收到的object.queue消息："+msg);
    }

    //延迟消息
    //死信监听器
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "dlx.queue",durable = "true"),
            exchange = @Exchange(name = "dlx.direct",type = ExchangeTypes.DIRECT),key = ("hi")))
    public void listenDlxQueue(String msg){
        log.info("消费者监听到了dlx.queue的消息：{}",msg);
    }

    //消息延迟插件
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue",durable = "true"),
            exchange = @Exchange(name = "delay.direct",type = ExchangeTypes.DIRECT,delayed = "true"),
            key = ("hi")))
    public void listenDelayQueue(String msg){
        log.info("消费者监听到了delay.queue的消息：{}",msg);
    }
}
