package com.steps.thread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @author fx
 * @date 2021-12-31 15:35
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i <10; i++) {
            new Thread(new MyThread(semaphore,i)).start();
        }
    }

    static class MyThread implements Runnable {
        private int value;
        private Semaphore semaphore;

        public MyThread(Semaphore semaphore, int value) {
            this.semaphore = semaphore;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire(); // 获取permit
                System.out.println(String.format("当前线程是%d, 还剩%d个资源，还有%d个资源在等待",value, semaphore.availablePermits(), semaphore.getQueueLength()));
                // 睡眠随机时间，打乱释放顺序
                Random random = new Random();
                Thread.sleep(random.nextInt(1000));
                semaphore.release(); // 释放permit
                System.out.println(String.format("线程%d释放了资源", value));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}

