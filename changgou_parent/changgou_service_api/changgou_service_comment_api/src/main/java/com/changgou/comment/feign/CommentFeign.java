package com.changgou.comment.feign;

import com.changgou.comment.pojo.Comment;
import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("comment")
public interface CommentFeign {

    @GetMapping("/comment/list")
    public Result<List<Comment>> list(@RequestParam("spuId") String spuId);

    @GetMapping("/comment/findLevel")
    public Result<List<Map>> findLevel(@RequestParam("spuId") String spuId);

    @GetMapping("/comment/findSpuLevel")
    public Result<List<Comment>> findSpuLevel(@RequestParam("level") String level,@RequestParam("spuId")String spuId);


    @GetMapping("/comment/getGoodLevel")
    public Result<Integer> getGoodLevel(@RequestParam("spuId") String spuId);

    @PostMapping("/comment/add")
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("content")String content, @RequestParam("level")String level);

}
