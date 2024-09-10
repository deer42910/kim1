package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryByid(Long id) {
        //缓存穿透
        //Shop shop = queryWithPassThrough(id);
        //互斥锁解决缓存击穿
        Shop shop = queryWithMutex(id);
        if (shop == null) {
            Result.fail("店铺不存在！");
        }
        return Result.ok(shop);
    }

    /**
     * 缓存击穿  try catch快捷键 选中 ctrl+alt+t
     * @param id
     * @return
     */
    public Shop queryWithMutex(Long id){
        //1.从redis查询缓存  完全可以使用hash 我们这里使用String
        String key = CACHE_SHOP_KEY+id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(shopJson)){
            //3.存在，直接返回
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //缓存穿透：判断命中的是否为空值
        if(shopJson!=null){
            //返回一个错误信息
            return null;
        }
        //4.实现缓存重建
        //4.1获取互斥锁
        String lockKey = "lock:shop:"+id;  //每一个商品都有一个锁
        Shop shop = null;
        try {
            boolean isLock = tryLock(lockKey);
            //4.2判断是否获取成功
            if (!isLock){
                //4.3失败，则休眠并重试
                Thread.sleep(50);
                return queryWithMutex(id);//递归操作
            }

            //4.4成功，根据id查询数据库
            shop = getById(id);
            //5.不存在，返回错误 就是穿透：也可以直接设置为null，这样不管存不存在都会加入到缓存
            if(shop == null){
                // 缓存穿透 将空值写入redis
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
                return null;
            }
            //6.存在，写入redis
            stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e){ //线程的中断情况:调用如 Thread.sleep(), Object.wait(), Thread.join() 等方法时，如果线程被中断
            throw new RuntimeException(e);
        }finally {
            //7.释放互斥锁
            unlock(lockKey);
        }

        //8.返回用户信息
        return shop;
    }
    /**
     * 将 缓存穿透代码 封装，防止以后找不到
     * @param id
     * @return
     */
    public Shop queryWithPassThrough(Long id){
        //1.从redis查询缓存  完全可以使用hash 我们这里使用String
        String key = CACHE_SHOP_KEY+id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(shopJson)){
            //3.存在，直接返回
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //缓存穿透：判断命中的是否为空值
        if(shopJson!=null){
            //返回一个错误信息
            return null;
        }
        //4.不存在，根据id查询数据库
        Shop shop = getById(id);
        //5.不存在，返回错误 就是穿透：也可以直接设置为null，这样不管存不存在都会加入到缓存
        if(shop == null){
            // 缓存穿透 将空值写入redis
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }
        //6.存在，写入redis
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);
        //7.返回用户信息
        return shop;
    }

    /**
     * 尝试获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key){
        //setIfAbsent()若不存在则设置为一，存在不设置  与redis中setnx对应 setnx string 1
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return BooleanUtil.isTrue(flag);  //判断是否为true，处理null，->false
    }

    /**
     * 释放锁(删除key)
     * @param key
     */
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }
    @Override
    @Transactional //通过事务控制原子性
    public Result update(Shop shop) {
        Long id = shop.getId();
        if(id == null){
            return Result.fail("店铺不能为空！");
        }
        //更新数据库
        updateById(shop);
        //删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY+id);
        return Result.ok();
    }
}
