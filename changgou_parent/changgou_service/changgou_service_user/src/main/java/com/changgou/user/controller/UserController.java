package com.changgou.user.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.service.UserService;
import com.changgou.user.pojo.User;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('accountant')")
    public Result findAll(){
        List<User> userList = userService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",userList) ;
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @GetMapping("/findById/{username}")
    public Result<User> findById(@PathVariable("username") String username){
        User user = userService.findById(username);
        return new Result(true,StatusCode.OK,"查询成功",user);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username){
        User user = userService.findById(username);
        return user;
    }



    /***
     * 新增数据  注册
     * @param user
     * @return
     */
    @PostMapping("/add/{verifyCode}")
    public Result add(@RequestBody User user,@PathVariable("verifyCode") String verifyCode){

        String verifyCodeRedis=null;
        verifyCodeRedis  = (String)redisTemplate.opsForValue().get(user.getPhone() + "001");

        if(verifyCode.equals(verifyCodeRedis)){
            User user1 = findUserInfo(user.getUsername());
            if(user1!=null){
                return new Result(false,StatusCode.ERROR,"用户名已存在");
            }
//  创建时间 修改时间 不能为空
            user.setCreated(new Date());
            user.setUpdated(new Date());

//            密码加密
            String gensalt = BCrypt.gensalt();
            String saltPassword = BCrypt.hashpw(user.getPassword(), gensalt);
            user.setPassword(saltPassword);
            userService.add(user);

        }else {
            return new Result(false,StatusCode.ERROR,"验证码错误");

        }

        return new Result(true,StatusCode.OK,"注册成功");

    }


    /***
     * 修改数据
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        String username = tokenDecode.getUserInfo().get("username");
        user.setUsername(username);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}" )
    public Result delete(@PathVariable String username){
        userService.delete(username);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<User> list = userService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    /**
     * 添加收藏
     * @param id
     * @return
     */
    @GetMapping("/love")
    public Result addLoveSku(@RequestParam String id){
        //获取当前登录用户

        String   username = tokenDecode.getUserInfo().get("username");


        //调用方法将用户和喜爱商品id保存
      return   userService.addLoveSku(username,id);

    }

    /**
     * 查询收藏商品信息
     * @return
     */
    @GetMapping("/myCollection")
    public Result<List<Sku>> findMyCollection(){
        String username = tokenDecode.getUserInfo().get("username");
        List<Sku> skuList = userService.myCollection(username);

        return new Result(true,StatusCode.OK,"",skuList);
    }

    /**
     * 添加足迹
     * @param id
     * @return
     */
    @GetMapping("/AddFootmark")
    public void addFootmark(@RequestParam("id") String id){
        //获取当前登录用户
        String username = null;
        try {
            username = tokenDecode.getUserInfo().get("username");
        } catch (Exception e) {
            if (username==null){
              return;
            }
        }
            userService.addFootMark(username,id);

    }

    /**
     * 查看我的足迹
     * @return
     */
    @GetMapping("/myFootmark")
    public Result<List<Sku>> myFootmark (){
        String username = tokenDecode.getUserInfo().get("username");
        List<Sku> skuList = userService.myFootMark(username);
        return new Result<>(true, StatusCode.OK,"查询成功",skuList);

    }



}
