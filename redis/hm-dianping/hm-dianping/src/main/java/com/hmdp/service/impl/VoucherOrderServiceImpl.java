package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.SeckillVoucherMapper;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {


    @Resource
    private ISeckillVoucherService seckillVoucherService;
    @Autowired
    private RedisIdWorker redisIdWorker;

    /**
     * 实现秒杀下单
     * @param voucherId
     * @return
     */
    public Result seckillVoucher(Long voucherId){
        //1.查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        //2.判断秒杀是否开始  |  是否结束
        if(voucher.getBeginTime().isAfter(LocalDateTime.now())){
            //尚未开始 返回异常结果
            return Result.fail("秒杀尚未开始！");
        }
        if(voucher.getEndTime().isBefore(LocalDateTime.now())){
            return Result.fail("秒杀已经结束！");
        }
        //3.开始 则判断库存是否充足
        if(voucher.getStock()<1) {
            //库存不足
            return Result.fail("库存不足！");
        }
        //4.充足 扣减库存
        /*第一种
        boolean success = seckillVoucherService.update().setSql("stock = stock -1") //set stock = stock-1
                .eq("voucher_id", voucherId)
                .eq("stock",voucher.getStock()).update();//where id = ? and stock =？
        乐观锁解决超卖问题：只要我扣减库存时的库存和我查询到的库存一致，就意味这没有人修改过库存，安全
        * 但：会有很多失败的情况，原因是：在使用乐观锁的过程中 假设100个线程同时拿到100个库存，大家一起扣减，但100人中只有1人扣减成功
        * */
        /*第二种方式，直接将库存大于0的 -1*/
        boolean success = seckillVoucherService.update().setSql("stock = stock -1").eq("voucher_id", voucherId)
                .gt("stock", 0).update();//where id = ? and stock>0
        if(!success){
            //扣减库存
            return Result.fail("库存不足！");
        }
        //5.创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        //5.1订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        //5.2用户id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        //5.3代金券id
        voucherOrder.setId(voucherId);
        save(voucherOrder);

        return Result.ok(orderId);
    }
}
