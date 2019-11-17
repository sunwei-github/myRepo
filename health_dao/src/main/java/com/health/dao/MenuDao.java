package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/12 8:32
 */
public interface MenuDao {
    /**
     * 根据角色id查询菜单
     * @param ids                角色id集合
     */
    List<Map> findMenu(@Param("ids") String ids);

    /**
     * 查询所有可访问的菜单
     * @param id          角色id
     * @return
     */
    LinkedHashSet<Menu> findAllMenu(Integer id);

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findMenuAll();

    /**
     * 菜单分页
     * @param queryString
     * @return
     */
    Page<Menu> selectByCondition(String queryString);

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    void add(Menu menu);

    /**
     * 根据权限的id查询权限表的数据
     *
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 菜单编辑
     * @param menu
     */
    void edit(Menu menu);

    /**
     * 通过中间表t_role_menu判断t_role角色表和t_menu菜单表是否有关联
     * @param id
     * @return
     */
    Integer findCountByMenuId(Integer id);

    /**
     * 删除菜单
     * @param id
     */
    void deleteByPermissionId(Integer id);
}
