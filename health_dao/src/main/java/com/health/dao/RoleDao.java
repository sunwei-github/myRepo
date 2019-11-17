package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:46
 */
public interface RoleDao {

    /**
     * 根据用户id查询角色列表
     * @param id           用户id
     * @return             角色集合
     */
    Set<Role> findRoleByUserId(Integer id);

    /**
     * 根据用户名查询角色
     * @param username         用户名
     * @return                 角色集合
     */
    List<Role> findRoleByUsername(String username);

    /**
     * 根据用户名查询角色id集合
     * @param username       用户名
     * @return               角色id集合
     */
    List<Integer> findRoleIdByUsername(String username);

    /**
     * 角色分页
     * @param queryString
     * @return
     */
    Page<Role> selectByCondition(String queryString);

    /**
     * 新增角色
     * @param role
     */
    void add(Role role);

    /**
     * 通过中间表关联角色和权限
     * @param map
     */
    void setRoleAndPermission(Map<String, Integer> map);

    /**
     * 通过中间表关联角色和菜单
     * @param map
     */
    void setRoleAndMenu(Map<String, Integer> map);
    /**
     * 查询角色所关联的权限
     * @param roleId
     * @return
     */
    List<Integer> findPermissionIdByRoleId(Integer roleId);
    /**
     * 查询角色所关联的菜单
     * @param roleId
     * @return
     */
    List<Integer> findMenuIdByRoleId(Integer roleId);

    /**
     * 根据角色id查询权限数据
     * @param roleId
     * @return
     */
    Role findById(Integer roleId);

    /**
     * 编辑角色
     * @param role
     */
    void editRole(Role role);

    /**
     * 删除之前角色和权限的关联
     * @param id
     */
    void deleteT_role_permissionByRoleId(Integer id);

    /**
     * 删除之前角色和菜单的关联
     * @param id
     */
    void deleteT_role_menuByRoleId(Integer id);

    /**
     * 查询用户和角色关联表判断用户中是否有角色
     * @param roleId
     * @return
     */
    Integer findUserCountByRoleId(Integer roleId);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRoleById(Integer roleId);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();
}
