package com.steps.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  当一个线程调用exchange方法后，它是处于阻塞状态的，只有当另一
 *  个线程也调用了exchange方法，它才会继续向下执行。看源码可以发现它是使用
 *  park/unpark来实现等待状态的切换的，但是在使用park/unpark方法之前，使用了CAS检查
 *
 *  Exchanger类还有⼀个有超时参数的方法，如果在指定时间内没有另⼀个线程调用exchange，就会抛出⼀个超时异常
 *
 *  当有多个线程调用同⼀个实例的exchange方法，只有前两个线程会交换数据，第三个线程会进入阻塞状态
 *
 * </p>
 * @author fx
 * @date 2021-12-31 15:49
 */
public class ExchangerDemo {


    public static void main(String[] args) throws InterruptedException {

        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(()->{
            try {
                System.out.println("这是线程A,和线程B交换数据的数据："+ exchanger.exchange("这是来自线程A的数据！"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("线程A已启动，等待线程B交换数据！");
        TimeUnit.SECONDS.sleep(2);
        new Thread(()->{
            try {
                System.out.println("这是线程B,和线程A交换数据的数据："+ exchanger.exchange("这是来自线程B的数据！"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();



    }
}
