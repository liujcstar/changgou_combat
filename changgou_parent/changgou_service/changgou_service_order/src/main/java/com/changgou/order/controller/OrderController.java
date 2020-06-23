package com.changgou.order.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.OrderService;
import com.changgou.order.pojo.Order;
import com.github.pagehelper.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Order> orderList = orderService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", orderList);
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable("id") String id) {
        Order order = orderService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", order);
    }

    @Autowired
    private TokenDecode tokenDecode;

    /***
     * 新增数据
     * @param order
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Order order,@RequestParam("id") String id) {
        //获取登录人名称
        String username = tokenDecode.getUserInfo().get("username");
        order.setUsername(username);
        String orderId = orderService.add(order,id);
        return new Result(true, StatusCode.OK, "添加成功", orderId);
    }


    /***
     * 修改数据
     * @param order
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Order order, @PathVariable String id) {
        order.setId(id);
        orderService.update(order);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        orderService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap) {
        List<Order> list = orderService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Order> pageList = orderService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @PostMapping("/batchSend")
    public Result batchSend(@RequestBody List<Order> orders) {
        orderService.batchSend(orders);
        return new Result(true, StatusCode.OK, "发货成功");
    }

    /**
     * 根据username查询
     *
     * @return
     */
    @GetMapping("/myOrderList")
    public Result<List<Order>> myOrderList() {
        String username = tokenDecode.getUserInfo().get("username");
        List<Order> orderList = orderService.myOrderList(username);
        return new Result(true,StatusCode.OK,"查询我的订单成功",orderList);
    }

    /**
     * 取消订单
     * @param id
     * @param num
     */
    @RequestMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam("id") String id, @RequestParam("num") Integer num) {
        orderService.cancelOrder(id, num);
        return new Result(true,StatusCode.OK,"取消订单成功");
    }

    /**
     * 手动确认收货
     * @param id
     */
    @PutMapping("/confirmTask")
    public Result confirmTask(@RequestParam("id")String id) {
        Order order = orderService.findById(id);
        String username = order.getUsername();
        orderService.confirmTask(id,username);
        return new Result(true,StatusCode.OK,"确认收货成功");
    }

    /**
     * 订单分页查询加条件查询
     * @param searchMap
     * @param
     * @param
     * @return
     */
    @PostMapping(value = "/search2" )
    public PageResult findPage2(@RequestBody Map searchMap){
        Page<Order> pageList = orderService.findPage2(searchMap);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return pageResult;
    }



    @GetMapping (value = {"/kinggm/{time}","/kinggm"})
    public Result kinggm(@PathVariable(required = false,value = "time") String time){

        if(StringUtils.isNotEmpty(time)){
            String startTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time.split(",")[0]));
            String endTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time.split(",")[1]));
            LinkedHashMap<String, List> map = orderService.kinggm(startTime, endTime);
            return new Result(true,StatusCode.OK,"查询成功",map);
        }else {
            String startTime =getStatetime();
            String endTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) ;
            LinkedHashMap<String, List> map = orderService.kinggm(startTime, endTime);
            return new Result(true,StatusCode.OK,"查询成功",map);
        }

    }


    //    获取当前日期前7天
    public String getStatetime(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, - 7);

        Date time = c.getTime();

        String preDay = sdf.format(time);

        return preDay;

    }


    //显示顶订单状态统计
    //直接显示
    @GetMapping("/lookOrderStatus")
    public Result lookOrderStatus(){
        LinkedHashMap<String, Object> orderStatus = orderService.findOrderStatus();
        return new Result(true,StatusCode.OK,"显示成功",orderStatus);
    }




}
