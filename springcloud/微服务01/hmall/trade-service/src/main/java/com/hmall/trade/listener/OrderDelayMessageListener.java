package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.constants.MQConstants;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/23 23:04
 **/
@Component
@RequiredArgsConstructor
public class OrderDelayMessageListener {

    private final IOrderService orderService;
    private final PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.delay_Order_Queue_Name),
    exchange = @Exchange(name = MQConstants.delay_Exchange_Name,delayed = "true"),
    key = MQConstants.delay_Order_Key))
    public void listenOrderDelayMessage(Long orderId){
        //1.查询订单
        Order order = orderService.getById(orderId);
        //2.检查订单状态
        if(order==null||order.getStatus()!=1){
            //订单不存在或者已经支付
            return;
        }
        //3.未支付，需要查询支付流水状态
        PayOrderDTO payOrderDTO = payClient.queryPayOrderByBizOrderNo(orderId);
        //4.判断是否支付
        if(payOrderDTO!=null&&payOrderDTO.getStatus()==3){
            //4.1已支付，标记订单状态为已支付
            orderService.markOrderPaySuccess(orderId);
        }else {
            //4.2未支付，取消订单，回复库存
            orderService.cancelOrder(orderId);
        }
    }

}
