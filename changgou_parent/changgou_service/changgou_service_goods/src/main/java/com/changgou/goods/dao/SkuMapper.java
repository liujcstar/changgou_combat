package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuMapper extends Mapper<Sku> {

    //扣减库存并增加销量
    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id=#{skuId} and num>=#{num}")
    int decrCount(OrderItem orderItem);

    //回滚库存(增加库存并扣减销量)
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
    void resumeStockNum(@Param("skuId") String skuId, @Param("num")Integer num);

    @Update("update tb_sku set comment_num = comment_num + #{commentNum} where id = #{id}")
    void updateCommentCountBySkuId(Sku sku);

    @Select("select * from tb_sku order by sale_num DESC limit 0,4")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="sale_num",property="saleNum"),
            @Result(column="spu_id",property="spuId"),
            @Result(column="comment_num",property="commentNum"),
    })
    List<Sku> findHotGoods();

}
