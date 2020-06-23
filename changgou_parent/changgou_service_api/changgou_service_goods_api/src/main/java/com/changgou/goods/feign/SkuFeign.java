package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {

    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);

    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

    @PostMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username") String username);

    @RequestMapping("/sku/resumeStockNum")
    public Result resumeStockNum(@RequestParam("skuId") String skuId,@RequestParam("num")Integer num);

    @PostMapping("/sku/updateCommentCountBySkuId")
    public void updateCommentCountBySkuId(@RequestParam("skuId") String skuId,@RequestParam("count") Integer count);

    @RequestMapping("/sku/findSpuIdBySkuId")
    public String findSpuIdBySkuId(@RequestParam("skuId") String skuId);

    @GetMapping("/sku/hotGoods")
    public Result<List<Sku>> hotGoods();

}
