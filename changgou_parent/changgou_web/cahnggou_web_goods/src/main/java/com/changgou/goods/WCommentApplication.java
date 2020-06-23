package com.changgou.goods;

import com.changgou.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients({"com.changgou.goods.feign","com.changgou.comment.feign","com.changgou.user.feign"})
public class WCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WCommentApplication.class,args);
    }


    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }

}
