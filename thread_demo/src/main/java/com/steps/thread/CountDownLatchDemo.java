package com.steps.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     模拟游戏资源加载
 * </p>
 * @author fx
 * @date 2021-12-31 16:19
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(()->{
            try {
                System.out.println("开始加载资源...");
                latch.await();
                System.out.println("资源加载完成！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(new MyThread(latch,"加载地图资源")).start();
        new Thread(new MyThread(latch,"加载人物资源")).start();
        new Thread(new MyThread(latch,"加载音乐资源")).start();

    }

    static class MyThread implements Runnable{

        private String task;
        private CountDownLatch latch;

        public MyThread(CountDownLatch latch,String task){
            this.latch = latch;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(task+" - 任务完成！");
            latch.countDown();
        }
    }

}
