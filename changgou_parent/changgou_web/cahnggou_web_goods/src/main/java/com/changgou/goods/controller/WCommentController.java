package com.changgou.goods.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.comment.feign.CommentFeign;
import com.changgou.comment.pojo.Comment;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.service.DetailService;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wcomment")
@SuppressWarnings("all")
public class WCommentController {

    @Autowired
    private CommentFeign commentFeign;


    @Autowired
    private DetailService detailService;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;


    /**
     * 前往商品详情页
     *
     * @return
     */
    @GetMapping("/toDetail")
    public String toDetail(@RequestParam("level")String level,@RequestParam("skuId")String skuId, Model model) {

        String spuId = skuFeign.findSpuIdBySkuId(skuId);

        Map<String, Object> itemData = detailService.getItemData(spuId);


        //获取商品总评价
        List<Comment> commentList = commentFeign.list(spuId).getData();
        for (Comment comment : commentList) {
            String username = comment.getUsername();
            User user = userFeign.findById(username).getData();
            comment.setHead_pic(user.getHead_pic());
        }

        //查询好评度
        Integer goodLevel = commentFeign.getGoodLevel(spuId).getData();
        itemData.put("goodLevel",goodLevel );


        //封装商品总数
        itemData.put("commentCount", commentList.size());

        if (!level.equals("0")){
            commentList = commentFeign.findSpuLevel(level,spuId ).getData();
            for (Comment comment : commentList) {
                String username = comment.getUsername();
                User user = userFeign.findById(username).getData();
                comment.setHead_pic(user.getHead_pic());
            }
        }

        //根据查询到的skuId查询sku信息,评价页面的sku参数信息
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Comment comment : commentList) {
            //封装时间
            String format = simpleDateFormat.format(comment.getCreateTime());
            comment.setTime(format);
        }

        //封装sku信息
        Sku sku = skuFeign.findById(skuId).getData();
        String spec = sku.getSpec();
        Map specmap = JSON.parseObject(spec, Map.class);
        itemData.put("specMap", specmap);


        itemData.put("commentList", commentList);


        //准备评论等级聚合数据
        List<Map> levelMap = commentFeign.findLevel(spuId).getData();

        for (Map map : levelMap) {
            itemData.put("id" + map.get("_id").toString(), map.get("num"));
        }
        //防止某个评论等级没有（好评，中评，差评）
        if (!itemData.containsKey("id1")) {
            itemData.put("id1", 0);
        }
        if (!itemData.containsKey("id2")) {
            itemData.put("id2", 0);
        }
        if (!itemData.containsKey("id3")) {
            itemData.put("id3", 0);
        }

        model.addAttribute("itemData", itemData);
        //将spuId返回给界面
        model.addAttribute("spuId",spuId);
        return "item";

    }




    @GetMapping("/commentPage")
    public String commentPage(){

        return "center-order-evaluate";
    }



    @GetMapping("/addComment")
    @ResponseBody
    public Result addComment(String skuId, String content,String level){
        Result result = commentFeign.add(skuId, content, level);
        return result;
    }

}
