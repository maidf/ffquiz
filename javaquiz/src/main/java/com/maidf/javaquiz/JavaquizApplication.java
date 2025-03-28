package com.maidf.javaquiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
@MapperScan("com.maidf.javaquiz.mapper")
@EnableRedisHttpSession
@EnableCaching
public class JavaquizApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaquizApplication.class, args);
    }

}
