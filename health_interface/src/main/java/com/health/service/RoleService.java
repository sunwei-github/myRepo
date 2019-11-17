package com.health.service;

import com.health.entity.PageResult;
import com.health.pojo.Role;

import java.util.List;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:02
 */
public interface RoleService {
    /**
     * 根据用户id查询角色列表
     * @param id           用户id
     * @return             角色集合
     */
    Set<Role> findRoleByUserId(Integer id);

    /**
     * 根据用户名查询用户角色
     * @param username          用户名
     * @return                  角色集合
     */
    List<Role> findRoleByUsername(String username);

    /**
     * 角色分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增检查组
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

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
     *
     * @param roleId
     * @return
     */
    Role findById(Integer roleId);

    /**
     * 编辑角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    /**
     * 删除检查组
     *
     * @param id
     * @return
     */
    void deleteByRoleId(Integer id);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 根据用户名查询角色id集合
     * @param username                用户名
     * @return                        角色id集合
     */
    List<Integer> findRoleIdByUsername(String username);
}
