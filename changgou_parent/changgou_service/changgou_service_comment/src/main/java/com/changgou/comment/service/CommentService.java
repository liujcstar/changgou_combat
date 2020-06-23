package com.changgou.comment.service;

import com.changgou.comment.pojo.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    void add(String skuId, String spuId, String content, String level);

    List<Comment> list(String spuId);

    List<Map> findLevel(String spuId);

    List<Comment> findSpuLevel(String level, String spuId);

    void updateCommentTask();

    int getGoodLevel(String spuId);
}
