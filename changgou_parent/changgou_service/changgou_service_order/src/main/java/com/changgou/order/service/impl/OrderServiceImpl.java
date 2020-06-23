package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSON;
//import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.*;
import com.changgou.order.pojo.*;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.AlipayFeign;
import com.changgou.pay.feign.PayFeign;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 增加
     * @param order
     */
    @Override
    //@GlobalTransactional(name = "order_add")
    @Transactional
    public String add(Order order, String id) {
        //1.获取购物车的相关数据(redis)
        Map cartMap = cartService.list(order.getUsername());
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");

        //2.统计计算:总金额,总数量
        //3.填充订单数据并保存到tb_order
        order.setTotalNum((Integer) cartMap.get("totalNum"));
        order.setTotalMoney((Integer) cartMap.get("totalMoney"));
        order.setPayMoney((Integer) cartMap.get("totalMoney"));

        //判断totalNum数量是否为0
        Integer totalNum = (Integer) cartMap.get("totalNum");
        if (totalNum < 1) {
            //基于我的订单页面购物下单
            //查询数据库
            Order order1 = this.findById(id);
            order.setTotalNum(order1.getTotalNum());
            order.setTotalMoney(order1.getTotalMoney());
            order.setPayMoney(order1.getPayMoney());
            //填充订单项数据并保存到tb_order_item
            //按条件查询,获取订单列表
            Example example2 = new Example(OrderItem.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orderId", id);
            List<OrderItem> orderItems = orderItemMapper.selectByExample(example2);
            for (OrderItem orderItem : orderItems) {
                orderItemList.add(orderItem);
            }
        }
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setBuyerRate("0"); // 0:未评价  1:已评价
        order.setSourceType("1"); //1:WEB
        order.setOrderStatus("0"); //0:未完成 1:已完成 2:已退货
        order.setPayStatus("0"); //0:未支付 1:已支付
        order.setConsignStatus("0"); //0:未发货 1:已发货
        String orderId = idWorker.nextId()+"";
        order.setId(orderId);
        orderMapper.insertSelective(order);

        //4.填充订单项数据并保存到tb_order_item
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setIsReturn("0"); //0:未退货 1:已退货
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }

        //扣减库存并增加销量
        skuFeign.decrCount(order.getUsername());

        //int i =1/0;
        //添加任务数据
        System.out.println("向订单数据库中的任务表去添加任务数据");
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);

        Map map = new HashMap();
        map.put("username",order.getUsername());
        map.put("orderId",orderId);
        map.put("point",order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(map));
        taskMapper.insertSelective(task);

        //5.删除购物车数据(redis)
        redisTemplate.delete("cart_"+order.getUsername());

        //发送延迟消息
        rabbitTemplate.convertAndSend("","queue.ordercreate",orderId);
        return orderId;
    }


    /**
     * 修改
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Order> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Order> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Order>)orderMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Order> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Order>)orderMapper.selectByExample(example);
    }

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    @Transactional
    public void updatePayStatus(String orderId, String transactionId) {

        //1.查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null && "0".equals(order.getPayStatus())){
            //2.修改订单的支付状态
            order.setPayStatus("1");
            order.setOrderStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            order.setTransactionId(transactionId); //微信返回的交易流水号
            orderMapper.updateByPrimaryKeySelective(order);

            //3.记录订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("1");
            orderLog.setPayStatus("1");
            orderLog.setRemarks("交易流水号:"+transactionId);
            orderLog.setOrderId(orderId);
            orderLogMapper.insert(orderLog);
        }


    }

    @Autowired
    private PayFeign payFeign;
    @Autowired
    private AlipayFeign alipayFeign;

    @Override
    @Transactional
    public void closeOrder(String orderId) {
        /**
         * 1.根据订单id查询mysql中的订单信息,判断订单是否存在,判断订单的支付状态
         * 2. 基于微信查询订单信息(微信)
         * 2.1)如果当前订单的支付状态为已支付,则进行数据补偿(mysql)
         * 2.2)如果当前订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
         */
        System.out.println("关闭订单业务开启:"+orderId);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            throw new RuntimeException("订单不存在!");
        }
        if (!"0".equals(order.getPayStatus())){
            System.out.println("当前订单不需要关闭");
            return;
        }
        System.out.println("关闭订单校验通过:"+orderId);

        //基于微信查询订单信息
       Map wxQueryMap = (Map) payFeign.queryOrder(orderId).getData();
       System.out.println("查询微信支付订单:"+wxQueryMap);
        Map aliQueryMap = (Map) alipayFeign.queryOrder(orderId).getData();
        System.out.println("查询微信支付订单:"+wxQueryMap);

       //如果订单的支付状态为已支付,进行数据补偿(mysql)
        if ("SUCCESS".equals(wxQueryMap.get("trade_state"))){
            this.updatePayStatus(orderId,(String) wxQueryMap.get("transaction_id"));
            System.out.println("完成数据补偿");
        }

        //如果订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
        if ("NOTPAY".equals(wxQueryMap.get("trade_state"))){
            System.out.println("执行关闭");
            order.setUpdateTime(new Date());
            order.setOrderStatus("4"); //订单已关闭
            orderMapper.updateByPrimaryKeySelective(order);

            //新增订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("4");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);

            //恢复商品的库存
            OrderItem _orderItem = new OrderItem();
            _orderItem.setOrderId(orderId);
            List<OrderItem> orderItemList = orderItemMapper.select(_orderItem);

            for (OrderItem orderItem : orderItemList) {
                skuFeign.resumeStockNum(orderItem.getSkuId(),orderItem.getNum());
            }

            //基于微信关闭订单
            payFeign.closeOrder(orderId);

        }
        //如果支付宝订单的支付状态为未支付,则修改mysql中的订单信息,新增订单日志,恢复商品的库存,基于微信关闭订单
        if ("NOTPAY".equals(aliQueryMap.get("trade_state"))){
            System.out.println("执行关闭");
            order.setUpdateTime(new Date());
            order.setOrderStatus("4"); //订单已关闭
            orderMapper.updateByPrimaryKeySelective(order);

            //新增订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("4");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);

            //恢复商品的库存
            OrderItem _orderItem = new OrderItem();
            _orderItem.setOrderId(orderId);
            List<OrderItem> orderItemList = orderItemMapper.select(_orderItem);

            for (OrderItem orderItem : orderItemList) {
                skuFeign.resumeStockNum(orderItem.getSkuId(),orderItem.getNum());
            }

            //基于支付宝关闭订单
            alipayFeign.closeOrder(orderId);

        }

    }

    @Override
    @Transactional
    public void batchSend(List<Order> orders) {

        //判断每一个订单的运单号和物流公司的值是否存在
        for (Order order : orders) {
            if (order.getId() == null){
                throw new RuntimeException("订单号不存在!");
            }
            if (order.getShippingCode() == null || order.getShippingName() == null){
                throw new RuntimeException("请输入运单号或物流公司的名称");
            }
        }

        //进行订单状态的校验
        for (Order order : orders) {
            Order order1 = orderMapper.selectByPrimaryKey(order.getId());
            if (!"0".equals(order1.getConsignStatus()) || !"1".equals(order1.getOrderStatus())){
                throw new RuntimeException("订单状态不合法");
            }
        }

        //修改订单的状态为已发货
        for (Order order : orders) {
            order.setOrderStatus("2"); //已发货
            order.setConsignStatus("1");//已发货
            order.setConsignTime(new Date());
            order.setUpdateTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            //记录订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperateTime(new Date());
            orderLog.setOperater("admin");
            orderLog.setOrderStatus("2");
            orderLog.setConsignStatus("1");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insertSelective(orderLog);
        }
    }

    @Override
    @Transactional
    public void confirmTask(String orderId, String operator) {

        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            throw new RuntimeException("订单不存在");
        }
        if (!"1".equals(order.getConsignStatus())){
            throw new RuntimeException("订单未发货");
        }

        order.setConsignStatus("2"); //已送达
        order.setOrderStatus("3"); //已完成
        order.setUpdateTime(new Date());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        //记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setId(idWorker.nextId()+"");
        orderLog.setOperateTime(new Date());
        orderLog.setOperater(operator);
        orderLog.setOrderStatus("3");
        orderLog.setConsignStatus("2");
        orderLog.setOrderId(order.getId());
        orderLogMapper.insertSelective(orderLog);
    }

    @Autowired
    private OrderConfigMapper orderConfigMapper;

    @Override
    @Transactional
    public void autoTack() {
        /**
         * 1.从订单配置表中获取到订单自动确认的时间点
         * 2. 得到当前时间节点,向前数 ( 订单自动确认的时间节点 ) 天,作为过期的时间节点
         * 3. 从订单表中获取相关符合条件的数据 (发货时间小于过期时间,收货状态为未确认 )
         * 4.循环遍历,执行确认收货
         */
        OrderConfig orderConfig = orderConfigMapper.selectByPrimaryKey(1);

        //获取当前时间
        LocalDate now = LocalDate.now();

        LocalDate date = now.plusDays(-orderConfig.getTakeTimeout());

        //按条件查询,获取订单列表
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("consignTime",date);
        criteria.andEqualTo("orderStatus","2");
        List<Order> orderList = orderMapper.selectByExample(example);

        for (Order order : orderList) {
            this.confirmTask(order.getId(),"system");
        }

    }

    /**
     * 根据username查询
     *
     * @param username
     * @return
     */
    @Override
    public List<Order> myOrderList(String username) {

        //按条件查询,获取订单
        Example example1 = new Example(Order.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("username", username);
        List<Order> orderList = orderMapper.selectByExample(example1);

        for (Order orders : orderList) {
            //按条件查询,获取订单列表
            Example example2 = new Example(OrderItem.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orderId", orders.getId());
            List<OrderItem> orderItems = orderItemMapper.selectByExample(example2);
            orders.setOrderItems(orderItems);
        }
        return orderList;
    }

    /**
     * 页面取消订单
     * @param id
     * @param num
     */
    @Override
    public void cancelOrder(String id, Integer num){

        Example example = new Example(OrderItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", id);
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        for (OrderItem orderItem : orderItems) {
            skuFeign.resumeStockNum(orderItem.getSkuId(), num);
        }

        Order order = orderMapper.selectByPrimaryKey(id);
        order.setUpdateTime(new Date());
        order.setOrderStatus("4"); //订单已关闭
        orderMapper.updateByPrimaryKeySelective(order);

        //新增订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setId(idWorker.nextId() + "");
        orderLog.setOperater("system");
        orderLog.setOperateTime(new Date());
        orderLog.setOrderStatus("4");
        orderLog.setOrderId(order.getId());
        orderLogMapper.insert(orderLog);
    }
    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 订单id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 支付类型，1、在线支付、0 货到付款
            if(searchMap.get("payType")!=null && !"".equals(searchMap.get("payType"))){
                criteria.andEqualTo("payType",searchMap.get("payType"));
           	}
            // 物流名称
            if(searchMap.get("shippingName")!=null && !"".equals(searchMap.get("shippingName"))){
                criteria.andLike("shippingName","%"+searchMap.get("shippingName")+"%");
           	}
            // 物流单号
            if(searchMap.get("shippingCode")!=null && !"".equals(searchMap.get("shippingCode"))){
                criteria.andLike("shippingCode","%"+searchMap.get("shippingCode")+"%");
           	}
            // 用户名称
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
           	}
            // 买家留言
            if(searchMap.get("buyerMessage")!=null && !"".equals(searchMap.get("buyerMessage"))){
                criteria.andLike("buyerMessage","%"+searchMap.get("buyerMessage")+"%");
           	}
            // 是否评价
            if(searchMap.get("buyerRate")!=null && !"".equals(searchMap.get("buyerRate"))){
                criteria.andLike("buyerRate","%"+searchMap.get("buyerRate")+"%");
           	}
            // 收货人
            if(searchMap.get("receiverContact")!=null && !"".equals(searchMap.get("receiverContact"))){
                criteria.andLike("receiverContact","%"+searchMap.get("receiverContact")+"%");
           	}
            // 收货人手机
            if(searchMap.get("receiverMobile")!=null && !"".equals(searchMap.get("receiverMobile"))){
                criteria.andLike("receiverMobile","%"+searchMap.get("receiverMobile")+"%");
           	}
            // 收货人地址
            if(searchMap.get("receiverAddress")!=null && !"".equals(searchMap.get("receiverAddress"))){
                criteria.andLike("receiverAddress","%"+searchMap.get("receiverAddress")+"%");
           	}
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                criteria.andEqualTo("sourceType",searchMap.get("sourceType"));
           	}
            // 交易流水号
            if(searchMap.get("transactionId")!=null && !"".equals(searchMap.get("transactionId"))){
                criteria.andLike("transactionId","%"+searchMap.get("transactionId")+"%");
           	}
            // 订单状态
            if(searchMap.get("orderStatus")!=null && !"".equals(searchMap.get("orderStatus"))){
                criteria.andEqualTo("orderStatus",searchMap.get("orderStatus"));
           	}
            // 支付状态
            if(searchMap.get("payStatus")!=null && !"".equals(searchMap.get("payStatus"))){
                criteria.andEqualTo("payStatus",searchMap.get("payStatus"));
           	}
            // 发货状态
            if(searchMap.get("consignStatus")!=null && !"".equals(searchMap.get("consignStatus"))){
                criteria.andEqualTo("consignStatus",searchMap.get("consignStatus"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}

            // 数量合计
            if(searchMap.get("totalNum")!=null ){
                criteria.andEqualTo("totalNum",searchMap.get("totalNum"));
            }
            // 金额合计
            if(searchMap.get("totalMoney")!=null ){
                criteria.andEqualTo("totalMoney",searchMap.get("totalMoney"));
            }
            // 优惠金额
            if(searchMap.get("preMoney")!=null ){
                criteria.andEqualTo("preMoney",searchMap.get("preMoney"));
            }
            // 邮费
            if(searchMap.get("postFee")!=null ){
                criteria.andEqualTo("postFee",searchMap.get("postFee"));
            }
            // 实付金额
            if(searchMap.get("payMoney")!=null ){
                criteria.andEqualTo("payMoney",searchMap.get("payMoney"));
            }

        }
        return example;
    }
    /**
     * 后台订单分页查询和条件查询
     * @param searchMap
     * @param
     * @param
     * @return
     */
    @Override
    public Page<Order> findPage2(Map<String, Object> searchMap) {
        Integer currentPage = (Integer) searchMap.get("currentPage");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        PageHelper.startPage(currentPage,pageSize);
        Example exemple =new Example(Order.class);
        Example.Criteria criteria = exemple.createCriteria();
        if (StringUtils.isNotEmpty((String) searchMap.get("queryString2"))){
            criteria.andEqualTo("id",searchMap.get("queryString2"));
        }
        if (StringUtils.isNotEmpty((String) searchMap.get("value"))) {
            criteria.andEqualTo("orderStatus", searchMap.get("value"));
        }
        if (StringUtils.isNotEmpty((String) searchMap.get("value2"))) {
            criteria.andEqualTo("sourceType", searchMap.get("value2"));
        }
        if (StringUtils.isNotEmpty((String) searchMap.get("queryString"))) {
            criteria.orEqualTo("receiverContact",searchMap.get("queryString"));
            criteria.orEqualTo("receiverMobile",searchMap.get("queryString"));
        }
        //criteria.andEqualTo("orderStatus",searchMap.get("queryString"));
        Page<Order> orderList = (Page<Order>) orderMapper.selectByExample(exemple);
        return orderList;
    }



    /**
     * 统计图
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public LinkedHashMap<String, List> kinggm(String startTime, String endTime) {

        LinkedHashMap<String, List> map = new LinkedHashMap<>();


        List<Map<String, Object>> list = orderMapper.kinggm(startTime, endTime);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        for (Map<String, Object> objectMap : list) {
            String name = String.valueOf( objectMap.get("name"));

            if ("1".equals(name)) {
                names.add("待付款订单");
                values.add(objectMap.get("value"));
            }
            if ("2".equals(name)) {
                names.add("待发货订单");
                values.add( objectMap.get("value"));
            }
            if ("3".equals(name)) {
                names.add("已发货订单");
                values.add(objectMap.get("value"));

            }
            if ("4".equals(name)) {
                names.add("已完成订单");
                values.add( objectMap.get("value"));

            }
            if ("5".equals(name)) {
                names.add("已关闭订单");
                values.add( objectMap.get("value"));
            }


        }

        map.put("names", names);
        map.put("values", values);


        return map;
    }





    //订单状态统计
    @Autowired
    private OrderService orderService;

    //直接显示统计图
    @Override
    @Transactional
//    @RequestMapping("/getOrderStatistics")
    public LinkedHashMap<String, Object> findOrderStatus() {

        //使用模拟数据测试使用什么样的java对象转为饼形图所需的json数据格式
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

//        List<String> setmealNames = new ArrayList<>();
//        setmealNames.add("待付款订单");
//        setmealNames.add("待发货订单");
//        setmealNames.add("已发货订单");
//        setmealNames.add("已完成订单");
//        setmealNames.add("已关闭订单");
//        setmealNames.add("已关闭订单");
//        data.put("setmealNames", setmealNames);
//
//        List<Map<String, Object>> setmealCount = new ArrayList<>();
//        Map<String, Object> v0 = new HashMap<>();
//        v0.put("name", "待付款订单");
//        v0.put("value", 1);
//        Map<String, Object> v1 = new HashMap<>();
//        v1.put("name", "待发货订单");
//        v1.put("value", 2);
//        Map<String, Object> v2 = new HashMap<>();
//        v2.put("name", "已发货订单");
//        v2.put("value", 1);
//        Map<String, Object> v3 = new HashMap<>();
//        v3.put("name", "已完成订单");
//        v3.put("value", 2);
//        Map<String, Object> v4 = new HashMap<>();
//        v4.put("name", "已关闭订单");
//        v4.put("value", 1);
//        Map<String, Object> v5 = new HashMap<>();
//        v5.put("name", "已关闭订单");
//        v5.put("value", 3);
//        setmealCount.add(v0);
//        setmealCount.add(v1);
//        setmealCount.add(v2);
//        setmealCount.add(v3);
//        setmealCount.add(v4);
//        setmealCount.add(v5);
//
//        data.put("setmealCount", setmealCount);
//        return data;

//        return null;

        try {
            List<LinkedHashMap<String, Object>> setmealCount = orderMapper.findOrderStatus();

            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");//获取订单状态名称
                if (name.equals("0")) {
                    name = "待付款订单";
                }

                if (name.equals("1")) {
                    name = "待发货订单";
                }

                if (name.equals("2")) {
                    name = "已发货订单";
                }

                if (name.equals("3")) {
                    name = "已完成订单";
                }
                if (name.equals("4")) {
                    name = "已关闭订单";
                }
                if (name.equals("5")) {
                    name = "已评价订单";
                }

                map.put("name",name);


            }

            data.put("setmealCount", setmealCount);
            System.out.println(setmealCount);

            List<String> setmealNames = new ArrayList<>();


            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");//获取订单状态名称
                if (name.equals("0")) {
                    name = "待付款订单";
                }

                if (name.equals("1")) {
                    name = "待发货订单";
                }

                if (name.equals("2")) {
                    name = "已发货订单";
                }

                if (name.equals("3")) {
                    name = "已完成订单";
                }
                if (name.equals("4")) {
                    name = "已关闭订单";
                }
                if (name.equals("5")) {
                    name = "已评价订单";
                }


                setmealNames.add(name);
            }

            data.put("setmealNames", setmealNames);
            System.out.println("-----------------------------------");
            System.out.println(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
