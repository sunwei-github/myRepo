package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;

import java.util.List;

/**
 * @author W.Sun
 * @date 2019/10/31 9:21
 */
public interface CheckItemService {

    /**
     * 新增检查项
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 查询当前页检查项
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除检查项
     * @param id 检查项id
     */
    void deleteCheckItemById(Integer id);

    /**
     *根据id查找检查项
     * @param id 检查项id
     */
    CheckItem findCheckItemById(Integer id);

    /**
     * 更改检查项内容
     * @param checkItem 检查项
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项数据
     * @return 检查项数据集合
     */
    List<CheckItem> findAll();
}
