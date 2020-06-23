package com.changgou.comment.task;

import com.changgou.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommentCountTask {

    @Autowired
    private CommentService commentService;

    //假设每隔十秒执行一次数据库订单更新
    @Scheduled(cron = "0/40 * * * * ?")
    public void updateComment(){

        commentService.updateCommentTask();

    }

}
