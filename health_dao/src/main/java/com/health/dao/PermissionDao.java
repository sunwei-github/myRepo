package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:34
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限列表
     * @param id              角色id
     * @return                权限集合
     */
    Set<Permission> findPermissionByRoleId(Integer id);

    /**
     * 权限分页
     * @param queryString
     * @return
     */
    Page<Permission> selectByCondition(String queryString);

    /**
     * 新增权限
     *
     * @param permission
     * @return
     */
    void add(Permission permission);

    /**
     * 根据权限的id查询权限表的数据 CV
     *
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 权限编辑
     *
     * @param permission
     * @return
     */
    void edit(Permission permission);

    /**
     * 通过中间表t_role_permission判断t_role角色表和t_permission权限表是否有关联
     * @param id
     * @return
     */
    Integer findCountByPermissionId(Integer id);

    /**
     * 删除权限
     * @param id
     */
    void deleteByPermissionId(Integer id);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findPermissionAll();
}
