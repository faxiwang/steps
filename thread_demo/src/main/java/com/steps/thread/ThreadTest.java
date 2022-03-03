package com.steps.thread;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @date 2021-10-10 22:02
 */
public class ThreadTest {
    public static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        System.out.println("主线程启动，线程ID:" + Thread.currentThread().getId());

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("线程id："+Thread.currentThread().getId());
        }, pool);

        //thread();

        //runnable();

        //callblePool();

        //callble();

        //completableFuture();

        //completableFuture2();

        //completableFuture3();

        //completableFuture5();


        while (true){
            /*if (pool.isTerminated()){
                break;
            }*/
            if (((ThreadPoolExecutor)pool).getActiveCount() < 1){
                pool.shutdown();
                break;
            }
            TimeUnit.SECONDS.sleep(2);
        }

        System.out.println("主线程执行结束");
    }

    public static void thread() {
        new Thread(() -> {
            System.out.println("线程ID:" + Thread.currentThread().getId() + "开始执行业务");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("业务执行完成");
        }).start();
    }

    public static void runnable() {
       pool.execute(new RunnableTest());
    }

    /**
     * 执行异步任务 并返回结果
     * @throws Exception
     */
    public static void callble() throws Exception {
        System.out.println("开始执行2个异步任务");
        long start = System.currentTimeMillis();
        FutureTask<Map<String, Object>> task1 = new FutureTask<>(new CallableTest());
        new Thread(task1).start();

        FutureTask<Map<String, Object>> task2 = new FutureTask<>(new CallableTest());
        new Thread(task2).start();
        /**
         * 获取任务执行结果
         * 注意：get操作将阻塞当前线程，直到异步线程执行完成
         */
        System.out.println("任务1执行结果：" + task1.get().get("time"));
        System.out.println("任务2执行结果：" + task2.get().get("time"));
        System.out.println("2个任务执行完成,总耗时:" + (System.currentTimeMillis() - start) + "毫秒");

    }

    /**
     * 执行异步任务 并返回结果
     * @throws Exception
     */
    public static void callblePool() throws Exception {
        System.out.println("开始执行2个异步任务");
        long start = System.currentTimeMillis();
        Future<Map<String, Object>> task1 = pool.submit(new CallableTest());

        Future<Map<String, Object>> task2 = pool.submit(new CallableTest());

        /**
         * 获取任务执行结果
         * 注意：get操作将阻塞当前线程，直到异步线程执行完成
         */
        System.out.println("任务1执行结果：" + task1.get().get("time"));
        System.out.println("任务2执行结果：" + task2.get().get("time"));
        System.out.println("2个任务执行完成,总耗时:" + (System.currentTimeMillis() - start) + "毫秒");

    }


    public static void monitorThreadPool(){
        CompletableFuture.runAsync(new RunnableTest(),pool);




        int activeCount = ((ThreadPoolExecutor) pool).getActiveCount();
        System.out.println(activeCount);
    }

    /**
     * 异步任务回调
     * (res: 执行结果 exception:异常）
     *  handle：方法执行完成后的处理 参数为BiFunction 有返回值
     *  whenComplete:方法完成后的感知 参数为BiConsumer，输入消费型 无返回值
     *  exceptionally: 仅异步任务执行异常时调用
     */
    public static void completableFuture() throws Exception {
        Integer val1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行异步任务");
            //int i = 1 / 0;
            return 2;
        }, pool).handle((res,exception)->{
            //方法执行完成后的处理
            System.out.println("handle执行结果:" + res + "  异常：" + exception);
            if (res!=null){
                return res +10;
            }
            return 0;
        }).get();

        System.out.println("val1:"+val1);

        Integer val2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行异步任务");
            //int i = 1 / 0;
            return 2;
        }, pool).whenComplete((res, exception) -> {
            //方法完成后感知，无法修改返回值
            res = res+2;
            System.out.println("whenComplete执行结果:" + res + "  异常：" + exception);
        }).exceptionally(e -> {
            //可以获取异常，同时返回默认值
            System.out.println("异步任务执行异常："+e);
            return 1;
        }).get();

        System.out.println("val2:"+val2);
    }

    /**
     * 线程串行化
     * thenRun() 或 thenRunAsync()：不能获取到上一步的执行结果，且无返回值
     *
     * thenAccept() 或 thenAcceptAsync()：可以获取得到上一步的执行结果，无返回值
     *
     * thenApply() 或 thenApplyAsync()：既可以获取上一步的执行结果，又有返回值
     * @throws Exception
     */
    public static void completableFuture2() throws Exception {
        //无返回值
        CompletableFuture.supplyAsync(() -> {
            System.out.println("执行异步任务1，线程ID:" + Thread.currentThread().getId());

            return 2;
        }, pool).thenRunAsync(() -> {
            //不能获取上一个任务的执行结果，也无返回值
            System.out.println("执行异步任务2，线程ID:" + Thread.currentThread().getId());

        }, pool);

        CompletableFuture.supplyAsync(() -> {
            System.out.println("执行异步任务1，线程ID:" + Thread.currentThread().getId());

            return 2;
        }, pool).thenAcceptAsync(res -> {
            //可以获取上一个任务的执行结果，但无返回值
            System.out.println("执行异步任务2，线程ID:" + Thread.currentThread().getId());
            System.out.println("任务1的执行结果为:"+res);
        }, pool);

        Integer val = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行异步任务1，线程ID:" + Thread.currentThread().getId());
            return 2;
        }, pool).thenApplyAsync(res -> {
            //可以获取上一个任务的执行结果，有返回值
            System.out.println("执行异步任务2，线程ID:" + Thread.currentThread().getId());
            System.out.println("任务1的执行结果为:" + res);
            return res + 2;
        }, pool).get();
        System.out.println("任务2的执行结果:"+val);
    }

    /**
     * 两任务组合
     * runAfterBothAsync：任务1、任务2 执行结束后任务3才启动,且任务3无法获取到任务1和任务2的执行结果
     * thenAcceptBothAsync：任务1、任务2 执行结束后任务3才启动,任务3可以获取任务1和任务2的执行结果，但无法对结果进行处理
     * thenCombineAsync：任务1、任务2 执行结束后任务3才启动,任务3可以获取任务1和任务2的执行结果,且可以对结果进行处理
     */
    public static void completableFuture3() throws Exception {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1执行完成");
            return 10;
        }, pool);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2执行完成");
            return 20;
        }, pool);

        // 任务1 任务2 执行结束后任务3才启动,且任务3无法获取到任务1和任务2的执行结果
        future1.runAfterBothAsync(future2,()->{
            System.out.println("runAfterBothAsync任务3启动，线程ID:" + Thread.currentThread().getId());
        },pool);

        // 任务1 任务2 执行结束后任务3才启动,任务3可以获取任务1和任务2的执行结果
        future1.thenAcceptBothAsync(future2,(f1,f2)->{
            System.out.println("thenAcceptBothAsync任务3启动,任务1的结果:"+f1+" 任务2的结果:"+f2);
        },pool);

        // 任务1 任务2 执行结束后任务3才启动,任务3可以获取任务1和任务2的执行结果,且对结果进行处理，有返回值
        CompletableFuture<Integer> future = future1.thenCombineAsync(future2, (f1, f2) -> {
            System.out.println("thenCombineAsync任务3启动,任务1的结果:" + f1 + " 任务2的结果:" + f2);
            return f1 + f2;
        }, pool);
        System.out.println("thenCombineAsync任务3的执行结果:"+future.get());

    }

    /**
     * 两任务组合，只要任意一个完成，就执行任务3
     * runAfterEitherAsync: 任务1、任务2任意一个完成，任务3就启动,且任务3无法获取到任务1和任务2的执行结果,也无返回值
     * acceptEitherAsync: 任务1、任务2任意一个完成,任务3就启动,任务3可以获取任务1或任务2优先执行完成的结果，无返回值
     * applyToEitherAsync: 任务1、任务2任意一个完成,任务3就启动,任务3可以获取任务1或任务2优先执行完成的结果，有返回值
     * @throws Exception
     */
    public static void completableFuture4() throws Exception {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1执行完成");
            return 10;
        }, pool);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2执行完成");
            return 20;
        }, pool);

        // 任务1、任务2任意一个完成，任务3就启动,且任务3无法获取到任务1和任务2的执行结果
        future1.runAfterEitherAsync(future2,()->{
            System.out.println("runAfterEitherAsync任务3启动，线程ID:" + Thread.currentThread().getId());
        },pool);

        // 任务1、任务2任意一个完成,任务3就启动,任务3可以获取任务1或任务2优先执行完成的结果，无返回值
        future1.acceptEitherAsync(future2,(res)->{
            System.out.println("acceptEitherAsync任务3启动,任务1或任务2的结果:"+res);
        },pool);

        // 任务1、任务2任意一个完成,任务3就启动,任务3可以获取任务1或任务2优先执行完成的结果，有返回值
        CompletableFuture<Integer> future = future1.applyToEitherAsync(future2, res -> {
            System.out.println("applyToEitherAsync任务3启动,任务1或任务2的结果:"+res);
            return res + 100;
        }, pool);
        System.out.println("applyToEitherAsync任务3的执行结果:"+future.get());

    }

    /**
     * 多任务组合
     * CompletableFuture.anyOf: 任意一个任务执行完成即可
     * CompletableFuture.allOf: 全部任务执行完成
     * @throws Exception
     */
    public static void completableFuture5() throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1执行完成");
            return 10;
        }, pool);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2执行完成");
            return 20;
        }, pool);

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务3启动，线程ID:" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务3执行完成");
            return 30;
        }, pool);

        //任意任务执行完成即可
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);
        System.out.println("anyOf->任务1、2、3任意任务执行完成，执行结果"+anyOf.get());

        //全部提交并等待任务执行完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
        //等待所有任务执行完成
        allOf.get();

        System.out.println("allOf->任务1执行结果:"+future1.get() + " 任务2执行结果:"+future2.get() + " 任务3执行结果"+ future3.get());
        System.out.println("总耗时:"+(System.currentTimeMillis()-start));

    }


    /**
     * getActiveCount()	线程池中正在执行任务的线程数量
     * getCompletedTaskCount()	线程池已完成的任务数量，该值小于等于taskCount
     * getCorePoolSize()	线程池的核心线程数量
     * getLargestPoolSize()	线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过，也就是达到了maximumPoolSize
     * getMaximumPoolSize()	线程池的最大线程数量
     * getPoolSize()	线程池当前的线程数量
     * getTaskCount()	线程池已经执行的和未执行的任务总数
     */
    public void threadPoolMonitor(){
        ThreadPoolExecutor exec = ((ThreadPoolExecutor) pool);
        int activeCount =exec.getActiveCount();
        long completedTaskCount = exec.getCompletedTaskCount();
        int poolSize = exec.getPoolSize();

        System.out.println("正在执行任务的线程数量:" +activeCount);

    }


}


