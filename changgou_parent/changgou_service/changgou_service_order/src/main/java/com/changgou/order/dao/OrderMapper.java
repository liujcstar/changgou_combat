package com.changgou.order.dao;

import com.changgou.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends Mapper<Order> {

    @Select("SELECT pay_status name , COUNT(pay_status) value FROM tb_order WHERE update_time BETWEEN #{startTime} AND #{endTime} GROUP BY pay_status")
    public List<Map<String,Object>> kinggm(@Param("startTime") String startTime, @Param("endTime") String endTime);

    //直接显示
    @Select("SELECT o.order_status name,COUNT(o.order_status) value FROM tb_order o GROUP BY o.order_status")
    public List<LinkedHashMap<String, Object>> findOrderStatus();

}
