package com.steps.thread;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author fx
 * @date 2021-10-10 22:25
 */
public class CallableTest implements Callable<Map<String, Object>> {
    @Override
    public Map<String, Object> call() throws Exception {
        long start = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        // 模拟业务执行
        System.out.println("线程ID:" + Thread.currentThread().getId() + "开始执行业务");
        TimeUnit.SECONDS.sleep(1);
        String year = LocalDate.now().toString();
        map.put("time", year);
        System.out.println("业务执行完成,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return map;
    }
}
