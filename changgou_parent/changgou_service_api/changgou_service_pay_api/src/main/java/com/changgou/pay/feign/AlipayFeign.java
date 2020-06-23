package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
    @FeignClient(name = "pay")
    public interface AlipayFeign {

        @GetMapping("/alipay/nativePay")
        public Result nativePay(@RequestParam("orderId") String orderId, @RequestParam("money")Integer money);

        @GetMapping("/alipay/query/{orderId}")
        public Result queryOrder(@PathVariable("orderId") String orderId);

        @PutMapping("/alipay/close/{orderId}")
        public Result closeOrder(@PathVariable("orderId") String orderId);
    }
