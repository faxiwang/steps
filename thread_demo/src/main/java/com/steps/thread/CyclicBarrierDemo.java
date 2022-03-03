package com.steps.thread;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 如果在参与者（线程）在等待的过程中，Barrier被破坏，就会抛出BrokenBarrierException。可以⽤ isBroken() ⽅法检测Barrier是否被破坏。
 * 1. 如果有线程已经处于等待状态，调⽤reset⽅法会导致已经在等待的线程出现BrokenBarrierException异常。
 * 并且由于出现了BrokenBarrierException，将会导致始终⽆法等待。
 * 2. 如果在等待的过程中，线程被中断，也会抛出BrokenBarrierException异常，并且这个异常会传播到其他所有的线程。
 * 3. 如果在执⾏屏障操作过程中发⽣异常，则该异常将传播到当前线程中，其他线程会抛出BrokenBarrierException，屏障被损坏。
 * 4. 如果超出指定的等待时间，当前线程会抛出 TimeoutException 异常，其他线程会抛出BrokenBarrierException异常。
 * </p>
 *
 * @author fx
 * @date 2021-12-31 16:50
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("主线程id:" + Thread.currentThread().getId());
        Integer result = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务线程id:" + Thread.currentThread().getId());
            // 模拟异常情况
            // int i = 1 / 0;
            return 10;
        }).whenComplete((res, exception) -> {
            // 任务执行完成后回调
            System.out.println("任务执行结果:" + res + "  异常：" + exception);
            // 在此方法内修改res不会影响最终的执行结果
            // res = res + 10;
        }).get();

        System.out.println("执行结果" + result);
    }
}
