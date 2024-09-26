package com.hmall.api.client;

import com.hmall.api.config.DefaultFeignConfig;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.api.fallback.ItemClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @Author:kim
 * @Description: openFeign注解 简化
 * @DateTime: 2024/9/13 23:30
 **/
@FeignClient(value = "item-service",configuration = DefaultFeignConfig.class,fallbackFactory = ItemClientFallbackFactory.class)
public interface ItemClient {
    /**
     * 查询商品列表
     * @param ids
     * @return
     */
    @GetMapping("/items")
    List<ItemDTO> queryItemByIds(@RequestParam("ids") Collection<Long> ids);  //将这儿的List集合改为Collection是 参数为set等什么集合都可以

    /**
     * 扣减库存
     * @param items
     */
    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);

}
