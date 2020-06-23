package com.changgou.goods.dao;

import com.changgou.goods.pojo.Spu;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {

    @Update("update tb_spu set comment_num = comment_num + #{commentNum} where id = #{id}")
    void updateCommentCountBySpuId(Spu spu);
}
