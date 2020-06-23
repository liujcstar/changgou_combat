package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.changgou.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user")
public interface UserFeign {

    /**
     * 用户名查询
     *
     * @param username
     * @return
     */
    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PostMapping("/user/update")
    public Result update(@RequestBody User user);

    /**
     * 查询收藏商品信息
     *
     * @return
     */
    @GetMapping("/user/myCollection")
    public Result<List<Sku>> findMyCollection();

    /**
     * 查看我的足迹
     *
     * @return
     */
    @GetMapping("/user/myFootmark")
    public Result<List<Sku>> myFootmark();

    /**
     * 注册
     * @param user
     * @param verifyCode
     * @return
     */
    @PostMapping("/user/add/{verifyCode}")
    public Result add(@RequestBody User user, @PathVariable("verifyCode") String verifyCode);


    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/validateCode/send/{phoneNumber}")
    public Result send4Order(@PathVariable("phoneNumber")  String phoneNumber);

    /**
     * 发送6位验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/validateCode/send6/{username}")
    public Result send6Order(@PathVariable("username")  String phoneNumber);



    @GetMapping("/user/findById/{username}")
    public Result<User> findById(@PathVariable("username") String username);


}
