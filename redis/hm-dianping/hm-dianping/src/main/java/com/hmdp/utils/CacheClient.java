package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;

/**
 * @Author:kim
 * @Description: 缓存工具类
 * @DateTime: 2024/9/11 6:53
 **/
@Slf4j
@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //正常设置过期时间
    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,unit);
    }
    //设置逻辑过期
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    //缓存穿透
    public <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback,Long time, TimeUnit unit){ //Class<R> type 告诉他泛型的类型 Function<参数,返回值> 函数式接口
        //1.从redis查询缓存  完全可以使用hash 我们这里使用String
        String key = keyPrefix+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(json)){
            //3.存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        //缓存穿透：判断命中的是否为空值
        if(json!=null){
            //返回一个错误信息
            return null;
        }
        //4.不存在，根据id查询数据库
        R r = dbFallback.apply(id);
        //5.不存在，返回错误 就是穿透：也可以直接设置为null，这样不管存不存在都会加入到缓存
        if(r == null){
            // 缓存穿透 将空值写入redis
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }
        //6.存在，写入redis
        this.set(key,r,time,unit);
        //7.返回用户信息
        return r;
    }

    //缓存击穿
    //重建一个线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 逻辑过期解决缓存击穿
     * @param id
     * @return
     */
    public <R,ID> R queryWithLogicalExpire(String keyPrefix,ID id,Class<R> type,Function<ID,R> dbFallback,Long time, TimeUnit unit){
        //1.从redis查询缓存  完全可以使用hash 我们这里使用String
        String key = keyPrefix+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isBlank(json)){
            //3.不存在，返回null
            return null;
        }
        //4.命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        //JSONObject data = (JSONObject) redisData.getData();  //将转为json对象
        //Shop shop = JSONUtil.toBean(data, Shop.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);//将上面合并为一句话
        LocalDateTime expireTime = redisData.getExpireTime();
        //5.判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            //5.1未过期，直接返回店铺信息
            return r;
        }
        //5.2已过期 需要缓存重建
        //6.缓存重建
        //6.1获取互斥锁
        String lockKey = LOCK_SHOP_KEY+id;
        boolean isLock = tryLock(lockKey);
        //6.2判断是否获取成功
        if (isLock){
            //TODO:6.3成功，开启独立线程，实现缓存重建 (线程池)
            CACHE_REBUILD_EXECUTOR.submit(()->{
                //重建缓存
                try {
                    //查询数据库
                    R r1 = dbFallback.apply(id);
                    //写入redis  带逻辑过期时间的写
                    this.setWithLogicalExpire(key,r1,time,unit);  //过期时间不能写死
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    unlock(lockKey);
                }
            });
        }
        //6.4返回过期的的店铺信息
        return r;
    }

    /**
     * 缓存击穿获取锁  try catch快捷键 选中 ctrl+alt+t
     * @param id
     * @return
     */
    public <R,ID> R queryWithMutex(String keyPrefix, ID id,Class<R> type,Function<ID,R> dbFallback,Long time, TimeUnit unit){
        //1.从redis查询缓存  完全可以使用hash 我们这里使用String
        String key = keyPrefix+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(json)){
            //3.存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        //缓存穿透：判断命中的是否为空值
        if(json!=null){
            //返回一个错误信息
            return null;
        }
        //4.实现缓存重建
        //4.1获取互斥锁
        String lockKey = "lock:shop:"+id;  //每一个商品都有一个锁
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            //4.2判断是否获取成功
            if (!isLock){
                //4.3失败，则休眠并重试
                Thread.sleep(50);
                return queryWithMutex(key,id,type,dbFallback,time,unit);//递归操作
            }

            //4.4成功，根据id查询数据库
            r = dbFallback.apply(id);
            //5.不存在，返回错误 就是穿透：也可以直接设置为null，这样不管存不存在都会加入到缓存
            if(r == null){
                // 缓存穿透 将空值写入redis
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
                return null;
            }
            //6.存在，写入redis
            this.set(key,r,time,unit);
        } catch (InterruptedException e){ //线程的中断情况:调用如 Thread.sleep(), Object.wait(), Thread.join() 等方法时，如果线程被中断
            throw new RuntimeException(e);
        }finally {
            //7.释放互斥锁
            unlock(lockKey);
        }
        //8.返回用户信息
        return r;
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
}
