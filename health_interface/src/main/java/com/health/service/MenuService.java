package com.health.service;

import com.health.entity.PageResult;
import com.health.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/12 8:12
 */
public interface MenuService {
    /**
     * 根据角色id查询可访问的菜单页
     * @param ids          角色id字符串
     * @return
     */
    List<Map> findMenu(String ids);

    /**
     * 查询所有可访问菜单
     * @param id             角色id
     * @return
     */
    LinkedHashSet<Menu> findAllMenu(Integer id);

    /**
     * 菜单分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findMenuAll();

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    void add(Menu menu);

    /**
     * 根据菜单的id查询权限表的数据
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 菜单编辑
     *
     * @param menu
     * @return
     */
    void edit(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void deleteByMenuId(Integer id);
}
