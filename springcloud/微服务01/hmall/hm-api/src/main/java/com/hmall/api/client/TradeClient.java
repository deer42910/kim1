package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/14 10:45
 **/
@FeignClient(value = "trade-service")
public interface TradeClient {
    @PutMapping("/orders/{orderId}")
    void markOrderSuccess(@PathVariable("orderId") Long orderId);
}
