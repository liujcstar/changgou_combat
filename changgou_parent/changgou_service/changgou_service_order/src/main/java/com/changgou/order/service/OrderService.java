package com.changgou.order.service;

import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.Order;
import com.github.pagehelper.Page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface OrderService {

    /***
     * 查询所有
     * @return
     */
    List<Order> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Order findById(String id);

    /***
     * 新增
     * @param order
     */
    String add(Order order,String id);

    /***
     * 修改
     * @param order
     */
    void update(Order order);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Order> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(Map<String, Object> searchMap, int page, int size);

    //修改订单的支付状态,并记录日志
    void updatePayStatus(String orderId, String transactionId);

    void closeOrder(String message);

    void batchSend(List<Order> orders);

    //手动确认收货
    void confirmTask(String orderId,String operator);

    void autoTack();

    /**
     * 根据username查询
     * @param username
     * @return
     */
    List<Order> myOrderList(String username);

    /**
     * 页面取消订单
     * @param orderId
     * @param num
     */
    void cancelOrder(String orderId, Integer num);

    /**
     * 后台订单分页查询加条件查询
     * @param searchMap
     * @param
     * @param
     * @return
     */
    Page<Order> findPage2(Map<String, Object> searchMap);



    /**
     * 订单统计图
     * @param startTime
     * @param endTime
     * @return
     */
    LinkedHashMap<String,List> kinggm(String startTime, String endTime);


    //直接显示
    public LinkedHashMap<String,Object> findOrderStatus();




}
