package com.hmdp;

import com.hmdp.utils.RedisIdWorker;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/12 16:04
 **/
@SpringBootTest
public class RedisIdWorkerTest {

    @Resource
    private RedisIdWorker redisIdWorker;
    //线程池
    private ExecutorService es = Executors.newFixedThreadPool(500);

    /**
     * 测试分布式id生成器的性能，以及可用性
     */
    @Test
    public void textNextId() throws InterruptedException {
        //使用CountDownLatch让线程同步等待
        CountDownLatch latch = new CountDownLatch(300);
        //创建线程任务
        Runnable task = ()->{
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id="+id);
            }
            //等待次数-1
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        //创建300个线程，每个线程创建100个id,总计生成3w个id
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        //线程阻塞，直到计时器归0时才会全部唤醒所有线程
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("生成3w个id共耗时"+(end-begin)+"ms");
    }

}
