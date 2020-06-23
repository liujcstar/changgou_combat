package com.changgou.web.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-21 16:46
 */
@Controller
@RequestMapping("/wuser")
public class UserController {

    @Autowired
    private UserFeign userFeign;


    /***
     * 新增数据  注册
     * @param user
     * @return
     */
    @PostMapping("/add/{verifyCode}")
    @ResponseBody
    public Result add(@RequestBody User user, @PathVariable("verifyCode") String verifyCode, Model model){

        Result result = userFeign.add(user, verifyCode);
        return result;
    }


    @RequestMapping("/toAdd")
    public String toAdd(){
        return "register";
    }


    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/send/{phoneNumber}")
    @ResponseBody
    public Result send4Order(@PathVariable("phoneNumber")  String phoneNumber){
        Result result = userFeign.send4Order(phoneNumber);
        return result;
    }


    /**
     * 客户聊天页面
     * @return
     */
    @RequestMapping("/talk")
    public String talk(){
        return "websocket_client";
    }

    /**
     * 客服人员聊天页面
     * @return
     */
    @RequestMapping("/talk2")
    public String talk1(){
        return "websocket_server";
    }


}
