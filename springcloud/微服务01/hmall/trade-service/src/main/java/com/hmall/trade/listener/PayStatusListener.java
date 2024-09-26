package com.hmall.trade.listener;

import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 14:26
 **/
@Component
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    //监听消息 ：定义direct类型交换机 命名为pay.direct
    //定义消息队列，命名为trade.pay.success.queue
    //将 队列 与 交换机 绑定 bindingKey为pay.success
    //支付成功将订单的id发送过来
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "trade.pay.success.queue",durable = "true"),
            exchange = @Exchange(name = "pay.direct",type = ExchangeTypes.DIRECT),
            key = "pay.success"
    ))
    public void listenPaySuccess(Long orderId){
        //1.查询订单
        Order order = orderService.getById(orderId);
        //2.判断订单模式，是否为未支付
        if(order==null||order.getStatus()!=1){ //不等于未支付，未支付的状态是1
            //不做处理  则只有在未支付的时候才会有这个标记
            return;
        }
        //3.标记为已支付
        orderService.markOrderPaySuccess(orderId);
    }
}
