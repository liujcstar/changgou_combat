package com.changgou.web.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wcart")
public class CartController {

    @Autowired
    private CartFeign cartFeign;

    //查询
    @GetMapping("/list")
    public String list(Model model) {
        Map map = cartFeign.list();
        model.addAttribute("items", map);
        return "cart";
    }


    //添加
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(String id, Integer num) {
        cartFeign.addCart(id, num);
        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK, "添加购物车成功", map);
    }

    /**
     * 通过改变数字来操作redis数据变动
     *
     * @param skuId
     * @param num
     * @return
     */
    @RequestMapping("/add2")
    @ResponseBody
    public Result<Map> add2(@RequestParam("id") String skuId, @RequestParam("num") Integer num) {
        Integer searchNum = 0;
        Map map = cartFeign.list();
        //遍历获取哪个商品被改变了

        List<Object> list = (List<Object>) map.get("orderItemList");
        for (Object obj : list) {
            OrderItem orderItem = new ObjectMapper().convertValue(obj, OrderItem.class);
            if (skuId.equals(orderItem.getSkuId())) {
                searchNum = orderItem.getNum();
            }
        }

        //做差得到应该变动的数值
        Integer resultNum = num - searchNum;
        return add(skuId, resultNum);
    }

    @RequestMapping("/toUserCenter")
    @ResponseBody
    public void toUserCenter() {
        System.out.println("成功调用了");
    }
}
