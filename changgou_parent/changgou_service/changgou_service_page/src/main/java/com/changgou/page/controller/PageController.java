package com.changgou.page.controller;


import com.changgou.page.service.impl.PageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/page")
@RestController
public class PageController {

    @Autowired
    private PageServiceImpl pageServiceimpl;

    @GetMapping("/getItemData")
    public Map<String,Object> getItemData(@RequestParam("spuId") String spuId){
        Map<String, Object> itemData = pageServiceimpl.getItemData(spuId);
        return itemData;
    }


}
