package com.changgou.web.user.controller;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/wuser")
public class UserCollection {

    @Autowired
    private UserFeign userFeign;

    /**
     * 查询收藏信息并展示在页面上
     * @param model
     * @return
     */
    @GetMapping("/collect")
    public String myCollection(Model model){
        Result<List<Sku>> result = userFeign.findMyCollection();

        List<Sku> skuList = result.getData();

        model.addAttribute("skuList",skuList);

        return "collect";

    }

    /**
     * 查看足迹
     * @param model
     * @return
     */
    @GetMapping("/footmark")
    public String Footmark(Model model){
        List<Sku> skuList = userFeign.myFootmark().getData();
        //将商品列表信息添加到model
        model.addAttribute("skuList",skuList);
        return "footmark";

    }
}
