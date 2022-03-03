package com.steps.redisson_demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fx
 * @date 2022-01-12 21:22
 */
@Configuration
public class MyRedissonAutoConfiguration {


    /**
     * 哨兵模式自动装配
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.useSingleServer().setPassword("redispwd");
        return Redisson.create(config);
    }


}
