package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {



    @Resource
    private ISeckillVoucherService seckillVoucherService;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //Redisson
    @Resource
    private RedissonClient redissonClient;

    //获取lua脚本
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    /*//获取阻塞队列
    //当一个线程尝试从一个队列中获取元素的时候，若这个队列没有元素，那么这个线程就会被阻塞，直到队列中有元素，才会被唤醒，然后执行元素
    //这个队列 有人下单的时候有 没人下单就没有
    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024*1024);


     private class VoucherOrderHandler implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    //1.获取队列中的订单信息
                    VoucherOrder voucherOrder = orderTasks.take();
                    //2.创建订单
                    handleVoucherOrder(voucherOrder);
                } catch (Exception e) {
                    log.error("处理订单异常",e);
                }
            }
        }
*/
    //异步下单 相关
    //线程池   executor处理器  小写转大写快捷键 ctrl+shift+U
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();//单线程， 速度不要特别快，给单线程足以

    @PostConstruct  //当前类初始化完成以后来执行
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    //线程任务
    private class VoucherOrderHandler implements Runnable{

        //队列名称
        String queueName = "stream.orders";

        @Override
        public void run() {
            while (true){
                try {
                    //1.获取消息队列中的订单信息 XREADGROUP GROUP g c1 COUNT 1 BLOCK 2000 STREAMS streams.order >
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    //2.判断消息获取是否成功
                    if(list==null||list.isEmpty()){
                        //2.1若获取失败，说明没有消息，继续下一次循环
                        continue;
                    }
                    //3.解析消息中的订单信息
                    MapRecord<String, Object, Object> record = list.get(0);  //MapRecord<String, Object, Object>发消息时就是键值对的欣赏形式
                    Map<Object, Object> value = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
                    //4.若获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
                    //5.ACK确认 SACK stream.orders g id
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g",record.getId());
                } catch (Exception e) {
                    log.error("处理订单异常",e);
                    handlePendingList();
                }
            }
        }


        private void handlePendingList() {
            while (true){
                try {
                    //1.获取pending-list中的订单信息 XREADGROUP GROUP g c1 COUNT 1 BLOCK 2000 STREAMS streams.order 0
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );
                    //2.判断消息获取是否成功
                    if(list==null||list.isEmpty()){
                        //2.1若获取失败，说明pending-list没有异常消息，结束循环
                        break;
                    }
                    //3.解析消息中的订单信息
                    MapRecord<String, Object, Object> record = list.get(0);  //MapRecord<String, Object, Object>发消息时就是键值对的欣赏形式
                    Map<Object, Object> value = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
                    //4.若获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
                    //5.ACK确认 SACK stream.orders g id
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g",record.getId());
                } catch (Exception e) {
                    log.error("处理pending-list异常",e);
                    try {
                        //太频繁 休眠20毫秒
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        //处理订单
        private void handleVoucherOrder(VoucherOrder voucherOrder) {
            //全新的线程 取用户id不应该在Holder中取
            //1.获取用户
            Long userId = voucherOrder.getUserId();
            //2.创建锁对象
            RLock lock = redissonClient.getLock("lock:order:" + userId);
            //3.尝试获取锁
            boolean isLock = lock.tryLock();
            //4.判断是否获取锁成功
            if(!isLock){
                //获取锁失败，返回错误或重试
                log.error("不允许重复下单");
                return;
            }
            try {
                proxy.createVoucherOrder(voucherOrder);
            }finally {
                //释放锁
                lock.unlock();
            }
        }



    }
    private IVoucherOrderService proxy;//成员变量，让方法能够拿到


    @Override
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = UserHolder.getUser().getId();

        //1.1查询订单
        int count = query().eq("user_id", userId).eq("voucher_id", voucherOrder).count();
        //1.2判断是否购买过一个
        if(count>0){
            //买过
            log.error("用户已经购买过一次了!");
            return;
        }

        //4.没买过 扣减库存
        boolean success = seckillVoucherService.update().setSql("stock = stock - 1")// set stock = stock - 1
                .eq("voucher_id", voucherOrder)
                .gt("stock", 0)// where id = ? and stock > 0
                .update();
        if (!success){
            //扣减失败
            log.error("库存不足！");
            return;
        }

        /*//5.创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        //5.1订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        //5.2用户id
        voucherOrder.setUserId(userId);
        //5.3代金券id
        voucherOrder.setId(voucherOrder);*/

        //保存
        save(voucherOrder);

        //7.返回订单
        //return Result.ok(orderId);
    }

    @Override
    public Result seckillVoucher(Long voucherId){
        //获取用户
        Long userId = UserHolder.getUser().getId();
        //获取订单id
        long orderId = redisIdWorker.nextId("order");
        //1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),//表示空集合
                voucherId.toString(), userId.toString() ,String.valueOf(orderId)
        );
        //2.判断结果是否为0
        int r = result.intValue();
        if(r!=0){
            //2.1不为零，代表没有资格
            return Result.fail(r==1?"库存不足":"不能重复下单");
        }

        /*此处省略的在Lua脚本中实现*/

        //获取代理对象（事务）
        proxy = (IVoucherOrderService) AopContext.currentProxy();

        //3.返回订单id
        return Result.ok(0);

        /*
        //Lua脚本，基于阻塞队列实现秒杀优化
        //获取用户
        Long userId = UserHolder.getUser().getId();
        //1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),//表示空集合
                voucherId.toString(), userId.toString()
        );
        //2.判断结果是否为0
        int r = result.intValue();
        if(r!=0){
            //2.1不为零，代表没有资格
            return Result.fail(r==1?"库存不足":"不能重复下单");
        }
        //2.2为零，有购买资格，把下单信息保存到阻塞队列
        VoucherOrder voucherOrder = new VoucherOrder();
        //2.3订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        //2.4用户id
        voucherOrder.setUserId(userId);
        //2.5代金券id
        voucherOrder.setVoucherId(voucherId);

        //2.6放入阻塞队列
        orderTasks.add(voucherOrder);
        *//*以上是抢单的流程*//*

        //获取代理对象（事务）
        proxy = (IVoucherOrderService) AopContext.currentProxy();

        //3.返回订单id
        return Result.ok(0);*/
    }
    /*
     * 实现秒杀下单
     * @param voucherId
     * @return
     */
    //@Override
    /*public Result seckillVoucher1(Long voucherId){
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

        //一人一单逻辑
        //扩大锁的范围 在方法中的话，所释放，其他线程进来，但是订单还没被提交 所以我们写在方法的外面
        //先获取锁 - 创建订单 - 提交订单 -释放锁
        Long userId = UserHolder.getUser().getId();
        synchronized (userId.toString().intern()) {
            //获取代理对象（事务）
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }

        //创建锁对象
        //分布式锁  使不同服务器之间即时更新
        //SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
        //使用 Redisson 创建锁
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        //获取锁
        boolean isLock = lock.tryLock();
        //判断是否获取锁成功
        if(!isLock){
            //获取锁失败 返回错误或重试
            return Result.fail("不允许重复下单");
        }
        try {
            //获取代理对象（事务）
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            //释放锁  异常也释放
            lock.unlock();
        }*/


        /*//1.1用户id
        Long userId = UserHolder.getUser().getId();
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        //1.2判断是否存在
        if(count>0){
            //用户已经购买过
            return Result.fail("用户已经购买过一次！");
        }

        //4.充足 扣减库存
        *//*第一种
        boolean success = seckillVoucherService.update().setSql("stock = stock -1") //set stock = stock-1
                .eq("voucher_id", voucherId)
                .eq("stock",voucher.getStock()).update();//where id = ? and stock =？
        乐观锁解决超卖问题：只要我扣减库存时的库存和我查询到的库存一致，就意味这没有人修改过库存，安全
        * 但：会有很多失败的情况，原因是：在使用乐观锁的过程中 假设100个线程同时拿到100个库存，大家一起扣减，但100人中只有1人扣减成功
        * *//*
        *//*第二种方式，直接将库存大于0的 -1*//*
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
        //Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        //5.3代金券id
        voucherOrder.setId(voucherId);
        save(voucherOrder);

        return Result.ok(orderId);*/
    }

    /**
     * 一人一单 添加订单  适合使用悲观锁
     * 但一下方法添加锁，锁的粒度太粗了 会导致每个线程进来都会锁住
     * @param voucherId
     * @return
     */
    /*@Transactional
    public synchronized Result createVoucherOrder(Long voucherId){
        Long userId = UserHolder.getUser().getId();
        //1.1查询订单
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        //1.2判断是否购买过一个
        if(count>0){
            //买过
            return Result.fail("用户已经购买过一次了！");
        }

        //4.没买过 扣减库存
        boolean success = seckillVoucherService.update().setSql("stock = stock - 1")// set stock = stock - 1
                .eq("voucher_id", voucherId)
                .gt("stock", 0)// where id = ? and stock > 0
                .update();
        if (!success){
            //扣减失败
            return Result.fail("库存不足！");
        }

        //5.创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        //5.1订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        //5.2用户id
        voucherOrder.setUserId(userId);
        //5.3代金券id
        voucherOrder.setId(voucherId);
        save(voucherOrder);
        //7.返回订单
        return Result.ok(orderId);
    }*/

    /**
     * 优化上面代码：
     * intern()这个方法是从常量池中拿到数据
     * 若我们使用userId.toString()拿到的对象实际上是不同的对象
     * 我们要保证是同一把锁就从常量池中拿 使用intern()
     *
     * @param voucherOrder
     * @return
     */
   /* @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder){

        Long userId = UserHolder.getUser().getId();

        //1.1查询订单
        int count = ().eq("user_id", userId).eq("voucher_id", voucherOrder).count();
        //1.2判断是否购买过一个
        if(count>0){
            //买过
            log.error("用户已经购买过一次了!");
            return;
        }

        //4.没买过 扣减库存
        boolean success = seckillVoucherService.update().setSql("stock = stock - 1")// set stock = stock - 1
                .eq("voucher_id", voucherOrder)
                .gt("stock", 0)// where id = ? and stock > 0
                .update();
        if (!success){
            //扣减失败
            log.error("库存不足！");
            return;
        }*/

        /*//5.创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        //5.1订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        //5.2用户id
        voucherOrder.setUserId(userId);
        //5.3代金券id
        voucherOrder.setId(voucherOrder);*/

        //保存
        //save(voucherOrder);

        //7.返回订单
        //return Result.ok(orderId);

