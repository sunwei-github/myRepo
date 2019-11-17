package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckItem;

import java.util.List;

/**
 * @author W.Sun
 * @date 2019/10/31 10:02
 */
public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    void deleteCheckItemById(Integer id);

    CheckItem findCheckItemById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    /**
     * 根据检查项id查找该检查项所关联的检查组数量
     * @param id   检查项id
     * @return     关联的检查组数量
     */
    long findCountByCheckItemId(Integer id);

    /**
     * 根据检查组id查询所有检查项数据
     * @param id            检查组id
     * @return              检查项数据集合
     */
    List<CheckItem> findCheckItemByCheckGroupId(Integer id);
}
