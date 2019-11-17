package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/3 16:13
 */
public interface SetmealDao {
    /**
     * 查询分页结果
     *
     * @param queryString 查询条件
     */
    Page<Setmeal> findPage(String queryString);

    /**
     * 添加套餐
     *
     * @param setmeal 套餐数据
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与其对应的检查组数据
     *
     * @param map 套餐id和对应的检查组id
     */
    void setCheckGroupAndSetmeal(Map<String, Integer> map);

    /**
     * 根据id查询套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    Setmeal findSetmealById(Integer id);

    /**
     * 根据套餐id查询对应的检查项id集合
     *
     * @param id 套餐id
     * @return 检查项id集合
     */
    List<Integer> findCheckgroupIds(Integer id);

    /**
     * 编辑套餐数据
     *
     * @param setmeal 套餐数据
     */
    void edit(Setmeal setmeal);

    /**
     * 删除套餐与旧检查组关系数据
     *
     * @param id 套餐id
     */
    void deleteAssociation(Integer id);

    /**
     * 添加套餐与新检查组关系数据
     *
     * @param map 套餐id及其对应的检查组id
     */
    void addAssociation(Map<String, Integer> map);

    /**
     * 查询所有套餐数据
     *
     * @return 套餐数据集合
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详细信息
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    Setmeal findById(Integer id);

    /**
     * 查询套餐图表数据
     *
     * @return 套餐图表数据
     */
    List<Map<String, Object>> getSetmealReport();

    /**
     * 查询排名前四的热门套餐
     * @return
     */
    List<Map<String,Object>> findHotSetmeal();
}
