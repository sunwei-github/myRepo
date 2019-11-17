package com.health.service;

import com.health.entity.PageResult;
import com.health.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/10 16:00
 */
public interface UserService {
    /**
     * 根据用户名查找用户
     * @param username          用户名
     * @return                  用户数据
     */
    User findUserByUsername(String username);

    /**
     * 查询当前登录用户的菜单信息
     * @return
     * @param username
     */
    List<Map> findMenu(String username);

    /**
     * 用户分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增用户
     * @param user
     * @param roleIds
     * @return
     */
    void add(User user, Integer[] roleIds);

    /**
     * 根据用户id查询用户数据
     * @param userId
     * @return
     */
    User findById(Integer userId);

    /**
     * 查询用户所关联的角色
     * @param userId
     * @return
     */
    List<Integer> findRoleIdsByUserId(Integer userId);

    /**
     * 编辑用户
     * @param user
     * @param roleIds
     * @return
     */
    void edit(User user, Integer[] roleIds);


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    void deleteByUserId(Integer id);
}
