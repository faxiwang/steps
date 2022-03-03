package com.steps.redisson_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class RedissonDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedissonDemoApplication.class, args);
	}

}
