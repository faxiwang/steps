package com.steps.thread;

import java.util.concurrent.TimeUnit;

/**
 * @date 2021-10-10 22:35
 */
public class RunnableTest implements Runnable {
    @Override
    public void run() {
        System.out.println("线程ID:"+Thread.currentThread().getId()+"开始执行业务");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("业务执行完成");
    }
}
