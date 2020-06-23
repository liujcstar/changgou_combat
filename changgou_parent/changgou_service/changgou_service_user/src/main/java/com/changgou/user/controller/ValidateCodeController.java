package com.changgou.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-21 15:40
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private RedisTemplate redisTemplate;

    //用户在线体检预约发送验证码
    @RequestMapping("/send/{username}")
    public Result send4Order(@PathVariable("username")  String phoneNumber){
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        //给用户发送验证码
       /* try{
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/

        //将验证码保存到redis（5分钟）//expire        001表示注册验证码
        redisTemplate.opsForValue().set(phoneNumber+"001",validateCode.toString(), 60 * 5, TimeUnit.SECONDS);


        return new Result(true, StatusCode.OK,"验证码发送成功",validateCode.toString());
    }
    //用户在线体检预约发送验证码
    @RequestMapping("/send6/{username}")
    public Result send6Order(@PathVariable("username")  String phoneNumber){
        //随机生成6位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);

        //给用户发送验证码
       /* try{
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/

        //将验证码保存到redis（5分钟）//expire        001表示注册验证码
        redisTemplate.opsForValue().set(phoneNumber+"002",validateCode.toString(), 60 * 5, TimeUnit.SECONDS);


        return new Result(true, StatusCode.OK,"验证码发送成功",validateCode.toString());
    }

}
