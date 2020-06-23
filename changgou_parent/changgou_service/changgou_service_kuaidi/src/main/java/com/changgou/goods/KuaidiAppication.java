package com.changgou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-20 0:10
 */
@SpringBootApplication
@EnableEurekaClient
public class KuaidiAppication {
    public static void main(String[] args) {
        SpringApplication.run(KuaidiAppication.class, args);
    }
}
