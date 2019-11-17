package com.health.service;

import com.health.entity.PageResult;
import com.health.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:02
 */
public interface PermissionService {
    /**
     * 根据角色id查询对应的权限列表
     * @param id            角色id
     * @return              权限集合
     */
    Set<Permission> findPermissionByRoleId(Integer id);

    /**
     * 权限分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

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
     * 删除权限
     *
     * @param id
     * @return
     */
    void deleteByPermissionId(Integer id);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findPermissionAll();
}
