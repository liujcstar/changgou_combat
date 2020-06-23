package com.changgou.comment.dao;

import com.changgou.comment.pojo.CommentMysql;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CommentMapper extends Mapper<CommentMysql> {
}
