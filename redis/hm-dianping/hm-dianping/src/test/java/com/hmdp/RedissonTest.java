package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/12 6:47
 **/
@Slf4j
@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;
    private RedissonClient redissonClient1;
    private RedissonClient redissonClient2;


    private RLock lock;

    @BeforeEach
    void setUp(){
        RLock lock = redissonClient.getLock("order");
        RLock lock1 = redissonClient1.getLock("order");
        RLock lock2 = redissonClient2.getLock("order");

        //创建连锁 multiLock
        lock = redissonClient.getMultiLock(lock,lock1,lock2); //使用哪个Client都可
    }
    @Test
    public void method1() throws InterruptedException {
        //尝试获取锁  错误是代理的问题（this.lock错误）
        boolean isLock = lock.tryLock(1L, TimeUnit.SECONDS);
        if (!isLock){
            log.error("获取锁失败....1");
            return;
        }
        try {
            log.info("获取锁成功.......1");
            method2();
            log.info("开始执行业务....1");
        } finally {
            log.warn("准备释放锁.....1");
            lock.unlock();
        }
    }
    void method2(){
        //尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock){
            log.error("获取锁失败....2");
            return;
        }
        try {
            log.info("获取锁成功.......2");
            log.info("开始执行业务....2");
        } finally {
            log.warn("准备释放锁.....2");
            lock.unlock();
        }
    }
}
