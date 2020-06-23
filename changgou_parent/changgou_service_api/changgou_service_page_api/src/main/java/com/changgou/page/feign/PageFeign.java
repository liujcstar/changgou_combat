package com.changgou.page.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("page")
public interface PageFeign {

    @GetMapping("/page/getItemData")
    public Map<Object,Object> getItemData(@RequestParam("spuId") String spuId);

}
