package com.hmdp.utils;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/11 21:48
 **/
public interface ILock {
    /**
     * 尝试获取锁
     * @param timeoutSec
     * @return
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();
}
