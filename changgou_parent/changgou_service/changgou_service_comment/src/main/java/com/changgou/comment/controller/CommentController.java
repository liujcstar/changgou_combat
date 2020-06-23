package com.changgou.comment.controller;

import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.service.CommentService;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SkuFeign skuFeign;


    @PostMapping("/add")
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("content")String content, @RequestParam("level")String level){
        String spuId = skuFeign.findSpuIdBySkuId(skuId);
        commentService.add(skuId,spuId,content,level);
        return new Result(true, StatusCode.OK,"评价成功");
    }

    /**
     * 查询所有评价
     * @param spuId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Comment>> list(@RequestParam("spuId") String spuId){
        List<Comment> commentList = commentService.list(spuId);
        return new Result(true,StatusCode.OK,"查询成功",commentList);
    }


    /**
     *  聚合查询好评差评
     */
    @GetMapping("/findLevel")
    public Result<List<Map>> findLevel(@RequestParam("spuId") String spuId){

        List<Map> mapList= commentService.findLevel(spuId);


        return new Result(true,StatusCode.OK,"查询成功",mapList);
    }

    /**
     * 查询指定评论内容
     */

    @GetMapping("/findSpuLevel")
    public Result<List<Comment>> findSpuLevel(@RequestParam("level") String level,@RequestParam("spuId")String spuId){

        List<Comment> commentList = commentService.findSpuLevel(level,spuId);

        return new Result<>(true,StatusCode.OK,"查询成功",commentList);
    }


    /**
     * 查询好评等级
     * @param spuId
     * @return
     */
    @GetMapping("/getGoodLevel")
    public Result<Integer> getGoodLevel(@RequestParam("spuId") String spuId){

        int goodLevel = commentService.getGoodLevel(spuId);

        return new Result<>(true,StatusCode.OK,"查询成功",goodLevel);
    }







}
