package com.hmdp;

import com.hmdp.utils.RedisIdWorker;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class HmDianPingApplicationTests {

    private RedisIdWorker redisIdWorker;

    private ExecutorService es = Executors.newFixedThreadPool(300);

    //@Test
    void testIdWorker() throws InterruptedException{
        //CountDownLatch 允许一个或多个线程等待直到一组操作完成。
        //初始化
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = ()->{
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id="+id);
            }
            latch.countDown();  //减少计数
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            es.submit(task);
        }
        latch.await();//计数为0 任务完成
        long end = System.currentTimeMillis();
        System.out.println("time=" + (end - begin));
    }
}
