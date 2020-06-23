package com.changgou.user.service;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.pojo.Task;
import com.changgou.user.pojo.User;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface UserService {

    /***
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 根据ID查询
     * @param username
     * @return
     */
    User findById(String username);

    /***
     * 新增
     * @param user
     */
    void add(User user);

    /***
     * 修改
     * @param user
     */
    void update(User user);

    /***
     * 删除
     * @param
     */
    void delete(String username);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<User> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(Map<String, Object> searchMap, int page, int size);

    int updateUserPoint(Task task);

    /**
     * 添加收藏商品
     * @param username
     * @param id
     * @return
     */
    Result addLoveSku(String username, String id);

    /**
     * 查询收藏商品信息
     * @param username
     * @return
     */
    public List<Sku> myCollection(String username);

    /**
     * 添加足迹
     * @param name
     * @param id
     */
    void addFootMark(String name,String id);

    public List<Sku> myFootMark(String username);

}