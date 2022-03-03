package com.steps.redisson_demo.config.contorller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author fx
 * @date 2022-01-12 22:02
 */
@RestController
@RequestMapping("/test")
public class RedissonContorller {

    @Autowired
    RedissonClient redisson;

    @RequestMapping("/test")
    public String test(){
        String key = "redisson:testlock:"+Thread.currentThread().getId();

        RLock lock = redisson.getLock(key);
        lock.lock(10,TimeUnit.SECONDS);
        try {
            System.out.println("线程："+ Thread.currentThread().getId() + "获取到锁");
            TimeUnit.SECONDS.sleep(5);
        }catch (Exception e){

        }finally {
            System.out.println("释放锁");
            lock.unlock();
        }

        return Thread.currentThread().getId() + "";
    }
}
