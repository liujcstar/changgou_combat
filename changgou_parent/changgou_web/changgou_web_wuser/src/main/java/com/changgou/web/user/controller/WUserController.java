package com.changgou.web.user.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Address;
import com.changgou.user.pojo.User;
import com.changgou.web.user.config.TokenDecode;
import com.changgou.web.user.service.WUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


/**
 * @author: phx
 * @date: 2020/6/20
 * @time: 10:32
 */
@Controller
@RequestMapping("/webuser")
@CrossOrigin
public class WUserController {

    @Autowired
    private WUserService wUserService;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private TokenDecode tokenDecode;
    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 跳转到个人信息配置页
     *
     * @return
     */
    @GetMapping("/toInfo")
    public String toInfo() {
        return "center-setting-info";
    }

    /**
     * 跳转到地址管理页面
     *
     * @return
     */
    @GetMapping("/toAddress")
    public String toUserCenter() {
        return "center-setting-address";
    }

    /**
     * 跳转到修改密码界面
     *
     * @return
     */
    @GetMapping("/toSafe")
    public String toSafe() {
        return "center-setting-safe";
    }

    /**
     * 修改密码验证手机号界面
     *
     * @return
     */
    @GetMapping("/toSafePhone")
    public String toSafePhone() {
        return "center-setting-safe-phone";
    }

    /**
     * 修改密码完成界面
     *
     * @return
     */
    @GetMapping("/toSafeComplete")
    public String toSafeComplete() {
        return "center-setting-safe-complete";
    }

    /**
     * 用户头像上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadImg")
    @ResponseBody
    public Result uploadImg(MultipartFile file) {
        String url = wUserService.uploadImg(file);
        return new Result(true, StatusCode.OK, "文件上传成功", url);
    }

    /**
     * 回显用户数据
     *
     * @return
     */
    @GetMapping("/getUser")
    @ResponseBody
    public User findUserByUsername() {
        String username = tokenDecode.getUserInfo().get("username");
        User user = userFeign.findUserInfo(username);
        user.setUsername(username);
        return user;
    }

    /**
     * 更改用户数据
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    @ResponseBody
    public Result updateUser(@RequestBody User user) {
        try {
            userFeign.update(user);
            return new Result(true, StatusCode.OK, "更新成功");
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "更新失败");
        }
    }

    /**
     * 查询当前登陆人的全部地址
     *
     * @return
     */
    @GetMapping("/selectAddress")
    @ResponseBody
    public Result<List<Address>> selectAddress() {
        Result<List<Address>> list = addressFeign.list();
        return addressFeign.list();
    }

    /**
     * 新增地址
     *
     * @param address
     */
    @PostMapping("/addAddress")
    public String addAddress(Address address) {
        addressFeign.add(address);
        return "center-setting-address";
    }

    /**
     * 回显地址数据
     *
     * @return
     */
    @PostMapping("/showAddress")
    @ResponseBody
    public Result<Address> showAddress(@RequestBody Address address) {
        return addressFeign.showAddress(address);
    }

    /**
     * 更新地址
     *
     * @param address
     */
    @PostMapping("/updateAddress")
    public String updateAddress(Address address) {
        addressFeign.updateAddress(address);
        return "center-setting-address";
    }

    /**
     * 删除地址
     *
     * @param address
     */
    @PostMapping("/deleteAddress")
    public String deleteAddress(@RequestBody Address address) {
        Integer id = address.getId();
        addressFeign.delete(id);
        return "center-setting-address";
    }

    /**
     * 修改默认地址
     *
     * @param address
     */
    @PostMapping("/setDefaultAddress")
    public String setDefaultAddress(@RequestBody Address address) {
        Integer id = address.getId();
        addressFeign.setDefaultAddress(id);
        return "center-setting-address";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestBody Map map) {
        String originPassword = (String) map.get("originPassword");
        String newPassword = (String) map.get("newPassword");
        String username = tokenDecode.getUserInfo().get("username");
        User userInfo = userFeign.findUserInfo(username);
        boolean flag = BCrypt.checkpw(originPassword, userInfo.getPassword());
        if (flag) {
            String gensalt = BCrypt.gensalt();
            userInfo.setPassword(BCrypt.hashpw(newPassword, gensalt));
            userFeign.update(userInfo);
            return new Result(true, StatusCode.OK, "密码修改成功");
        } else {
            return new Result(false, StatusCode.ERROR, "原密码不正确");
        }

    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/send")
    @ResponseBody
    public Result send(@RequestParam("phone") String phone) {
        Result result = userFeign.send6Order(phone);
        return result;
    }

    /**
     * 校验验证码
     *
     * @param map
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public Result check(@RequestBody Map map) {
        String code = (String) map.get("code");
        String phone = (String) map.get("phone");

        String redisCode = (String) redisTemplate.boundValueOps(phone + "002").get();
        if (redisCode.equals(code)) {
            return new Result(true, StatusCode.OK, "校验通过");
        }
        return new Result(false, StatusCode.ERROR, "校验失败");
    }

    /**
     * 更新手机号
     *
     * @param
     * @return
     */
    @GetMapping("/updatePhone")
    @ResponseBody
    public Result updatePhone(@RequestParam("newPhone") String newPhone) {
        if(StringUtils.isEmpty(newPhone)){
            return new Result(false,StatusCode.ERROR,"手机号不能为空");
        }
        String username = tokenDecode.getUserInfo().get("username");
        User userInfo = userFeign.findUserInfo(username);
        userInfo.setPhone(newPhone);
        Result result = userFeign.update(userInfo);
        return result;
    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String uid = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uid")) {
                uid = cookie.getValue();
            }
        }
        stringRedisTemplate.delete(uid);
        return "center-setting-info";
    }

}
