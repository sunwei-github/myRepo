package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/1 12:52
 */
public interface CheckGroupDao {
    Page<CheckGroup> selectByCondition(String queryString);

    /**
     * 添加新的检查组
     *
     * @param checkGroup 检查组数据
     */
    void add(CheckGroup checkGroup);

    /**
     * 根据检查组id，添加对应的检查项id
     *
     * @param map 检查组id及其对应的检查项id
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 根据检查组id查询检查组数据
     * @param  id         检查组id
     * @return CheckGroup 检查组数据
     */
    CheckGroup findCheckGroupById(Integer id);

    /**
     * 查询检查组与检查项的关联信息
     * @param id       检查组id
     * @return         检查项id数组
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组信息
     * @param checkGroup   检查组信息
     */
    void edit(CheckGroup checkGroup);

    /**
     * 删除旧的检查组与检查项关系
     * @param id       检查组id
     */
    void deleteAssociation(Integer id);

    /**
     * 添加新的检查组与检查项关系
     * @param map       检查组及其对应的检查项
     */
    void addAssociation(Map<String, Integer> map);

    /**
     * 删除检查组与对应套餐的数据
     * @param id    检查组id
     */
    void deleteRelationshipWithSetMeal(Integer id);

    /**
     * 输出检查组数据
     * @param id      检查组id
     */
    void deleteCheckGroup(Integer id);

    /**
     * 查询所有检查组
     * @return    检查组集合
     */
    List<CheckGroup> findAll();

    /**
     * 根据id查询，结果包括检查项数据
     * @param id            套餐id
     * @return              检查组集合
     */
    List<CheckGroup> findCheckGroupById2(Integer id);

}
