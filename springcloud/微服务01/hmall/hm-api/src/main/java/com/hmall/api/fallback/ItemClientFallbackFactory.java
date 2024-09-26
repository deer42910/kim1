package com.hmall.api.fallback;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/18 0:34
 **/
@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {  //注册这个bean 我们可以新写一个，懒，也可以放到DefaultFeignConfig中
    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
                log.error("查询商品失败！",cause);
                return CollUtils.emptyList();  //查询失败不会报异常，而是返回一个空列表
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.error("扣减商品库存失败！",cause);
                throw new RuntimeException(cause);//还不知道如何处理，先抛出异常
            }
        };
    }
}
