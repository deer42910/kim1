package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/11 21:50
 **/
public class SimpleRedisLock implements ILock{

    //不同的锁有不同的业务 所以锁名称要不同
    private String name;
    private StringRedisTemplate stringRedisTemplate;
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true)+"-";  //UUID中的横线去掉

    //锁脚本先定义好
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<Long>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识  uuid是区分不同服务器的 而线程id是相同服务器的不同业务
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        //获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.MINUTES);
        return Boolean.TRUE.equals(success);//注意：返回值类型是基本类型 这里是包装类，涉及到自动拆箱问题 会有安全问题 若success为null
        //这种方法 true 返回true false与null都返回false
    }

    @Override
    public void unlock() {
        //调用lua脚本
        stringRedisTemplate.execute(UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX+Thread.currentThread().getId());
       /* //获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getName();
        //获取锁中的标识
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        //判断标识是否一致
        if (threadId.equals(id)){
            //一致 释放锁
            stringRedisTemplate.delete(KEY_PREFIX+name);
        }*/
    }
}
