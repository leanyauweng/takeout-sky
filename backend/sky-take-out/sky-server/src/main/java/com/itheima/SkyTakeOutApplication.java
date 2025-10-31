package com.itheima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@EnableCaching //开启缓存注解功能
@EnableScheduling
@MapperScan("com.itheima.mapper")
public class SkyTakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyTakeOutApplication.class, args);
    }
}
