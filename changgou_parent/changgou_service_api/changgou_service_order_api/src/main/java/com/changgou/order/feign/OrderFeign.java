package com.changgou.order.feign;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "order")
public interface OrderFeign {



    @PostMapping("/order")
    public Result add(@RequestBody Order order,@RequestParam("id") String id);




    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable("id") String id);

    @GetMapping("/order/myOrderList")
    public Result<List<Order>> myOrderList();


    /**
     * 取消订单
     * @param id
     * @param num
     */
    @RequestMapping("/order/cancelOrder")
    public Result cancelOrder(@RequestParam("id") String id, @RequestParam("num") Integer num);

    /**
     * 订单分页加条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/order/search2" )
    public PageResult findPage2(@RequestBody Map searchMap);


    @GetMapping(value = {"/order/kinggm/{time}"})
    public Result kinggm(@PathVariable(required = false,value = "time") String time);


    //订单统计状态
    //直接显示
    @GetMapping("/order/lookOrderStatus")
    public Result lookOrderStatus();



    /**
     * 手动确认收货
     * @param
     */
    @PutMapping("/order/confirmTask")
    public Result confirmTask(@RequestParam("id") String id);
}
