package com.changgou.web.user;

import com.changgou.interceptor.FeignInterceptor;
import com.changgou.web.user.config.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;

/**
 * @author: phx
 * @date: 2020/6/20
 * @time: 10:24
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.user.feign")
public class    WUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(WUserApplication.class,args);
    }
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }
}
