package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/10 16:41
 */
public interface UserDao {
    /**
     * 根据用户名查找用户
     * @param username          用户名
     * @return                  用户信息
     */
    User findUserByUsername(String username);

    /**
     * 用户分页
     * @param queryString
     * @return
     */
    Page<User> selectByCondition(String queryString);

    /**
     * 新增用户
     * @param user
     */
    void add(User user);

    /**
     * 通过中间表关联用户和角色
     * @param map
     */
    void setUserAndRole(Map<String, Integer> map);

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
     */
    void editUser(User user);

    /**
     * 删除之前用户和角色的关联
     * @param id
     */
    void deleteT_user_roleByRoleId(Integer id);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    void deleteUserById(Integer id);
}
