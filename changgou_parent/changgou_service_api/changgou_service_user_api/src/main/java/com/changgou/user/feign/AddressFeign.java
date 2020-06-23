package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user")
public interface AddressFeign {
    /**
     * 查询当前用户地址列表
     * @return
     */
    @GetMapping("/address/list")
    public Result<List<Address>> list();

    /**
     * 新增地址
     * @param address
     * @return
     */
    @PostMapping("/address")
    public Result add(@RequestBody Address address);

    /**
     * 回显地址数据
     * @param address
     * @return
     */
    @PostMapping("/address/showAddress")
    public Result<Address> showAddress(@RequestBody Address address);

    /***
     * 修改数据
     * @param address
     * @return
     */
    @PostMapping("/address/updateAddress")
    public Result updateAddress(@RequestBody Address address);

    /***
     * 根据ID删除地址数据
     * @param id
     * @return
     */
    @PostMapping("/address/deleteAddress")
    public Result delete(@RequestParam("id") Integer id);

    /**
     * 修改默认地址
     * @param id
     */
    @PostMapping("/address/setDefaultAddress")
    public void setDefaultAddress(@RequestParam("id") Integer id);
}
