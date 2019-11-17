package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author W.Sun
 * @date 2019/11/1 12:41
 */
public interface CheckGroupService {
    /**
     * 查询指定页的检查组数据
     * @param queryPageBean  分页参数
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 新增检查组
     * @param checkGroup     检查组数据
     * @param checkitemIds   检查项id数组
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 查询检查组
     * @param id    检查组id
     * @return      检查组数据
     */
    CheckGroup findCheckGroupById(Integer id);

    /**
     * 根据检查组id查询与之相关联的检查项id
     * @param id     检查组id
     * @return       检查项id数组
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 更改检查组信息
     * @param checkGroup              检查组信息
     * @param checkItemIds            检查组所关联的检查项id集合
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 删除检查组
     * @param id    检查组id
     */
    void delete(Integer id);

    /**
     * 查询所有检查组
     * @return     检查组集合
     */
    List<CheckGroup> findAll();

}
