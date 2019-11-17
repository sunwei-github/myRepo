package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/3 15:21
 */
public interface SetmealService {
    /**
     * 查询分页数据
     *
     * @param queryPageBean 分页参数
     * @return 分页结果
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 添加新套餐
     *
     * @param setmeal       套餐数据
     * @param checkgroupIds 套餐对应的检查组id数组
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据id查找套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    Setmeal findSetmealById(Integer id);

    /**
     * 根据套餐id查询对应的检查项id集合
     * @param id         套餐id
     * @return           检查项id集合
     */
    List<Integer> findCheckgroupIds(Integer id);

    /**
     * 编辑套餐数据及其与检查组的关系数据
     * @param setmeal          套餐数据
     * @param checkgroupIds    检查组id数组
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询所有套餐信息
     * @return            套餐数据
     */
    List<Setmeal> findAll() throws IOException;

    /**
     * 查询套餐的详细信息
     * @param id           套餐id
     * @return             套餐数据
     */
    Setmeal findById(Integer id) throws IOException;

    /**
     * 查询套餐数据报告图表
     * @return        图表数据
     */
    Map<String,Object> getSetmealReport();

    /**
     * 查询排行前四的热门套餐
     * @return
     */
    List<Map<String,Object>> findHotSetmeal();
}
