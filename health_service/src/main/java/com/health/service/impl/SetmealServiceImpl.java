package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.RedisConstant;
import com.health.dao.SetmealDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;

/**
 * @author W.Sun
 * @date 2019/11/3 15:24
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询
     *
     * @param queryPageBean 分页参数
     * @return 分页结果
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> pages = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    /**
     * 添加套餐
     *
     * @param setmeal       套餐数据
     * @param checkgroupIds 套餐对应的检查组id数组
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐
        setmealDao.add(setmeal);
        //获取添加成功后的套餐id
        Integer id = setmeal.getId();
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //添加对应的检查组数据
            setCheckGroupAndSetmeal(id, checkgroupIds);
        }
    }

    /**
     * 添加套餐与检查项关系数据
     *
     * @param id            套餐id
     * @param checkgroupIds 检查项id数组
     */
    private void setCheckGroupAndSetmeal(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("chechgroup_id", checkgroupId);
            map.put("setmeal_id", id);
            setmealDao.setCheckGroupAndSetmeal(map);
        }
    }

    /**
     * 根据id查询套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    @Override
    public Setmeal findSetmealById(Integer id) {
        return setmealDao.findSetmealById(id);
    }

    /**
     * 根据套餐id查询对应的检查项id集合
     *
     * @param id 套餐id
     * @return 检查项id集合
     */
    @Override
    public List<Integer> findCheckgroupIds(Integer id) {
        return setmealDao.findCheckgroupIds(id);
    }

    /**
     * 编辑套餐数据及其与检查组的关系数据
     *
     * @param setmeal       套餐数据
     * @param checkgroupIds 检查组id数组
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //编辑套餐数据
        setmealDao.edit(setmeal);
        Integer id = setmeal.getId();
        //清除套餐与检查组的旧关系数据
        setmealDao.deleteAssociation(id);
        //添加套餐检查组的新关系数据
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.addAssociation(map);
        }
    }

    /**
     * 查询所有套餐数据
     *
     * @return 套餐数据集合
     */
    @Override
    public List<Setmeal> findAll() throws IOException {
        String setMeal = jedisPool.getResource().get(RedisConstant.SETMEAL_NAME);
        if (setMeal == null) {
            //从数据库查询
            List<Setmeal> setmealList = setmealDao.findAll();
            //将集合转换成字符串存放
            setMeal = new ObjectMapper().writeValueAsString(setmealList);
            //存放到redis
            jedisPool.getResource().set(RedisConstant.SETMEAL_NAME, setMeal);
        }
        //从redis取出数据返回
        List<Setmeal> setmeals = new ObjectMapper().readValue(setMeal, new TypeReference<List<Setmeal>>() {
        });
        return setmeals;
    }

    /**
     * 查询套餐的详细信息
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    @Override
    public Setmeal findById(Integer id) throws IOException {
        String setmealDetail = jedisPool.getResource().get(RedisConstant.SETMEAL_DETAIL + id);
        if (setmealDetail == null) {
            //从数据库查询
            Setmeal setmeal = setmealDao.findById(id);
            //将集合转换成字符串存放
            setmealDetail = new ObjectMapper().writeValueAsString(setmeal);
            //存放到redis
            jedisPool.getResource().set(RedisConstant.SETMEAL_DETAIL + id, setmealDetail);
        }
        //从redis取出数据返回
        Setmeal setmeal = new ObjectMapper().readValue(setmealDetail, Setmeal.class);
        return setmeal;
    }


    /**
     * 查询套餐数据图表
     *
     * @return 套餐图表数据
     */
    @Override
    public Map<String, Object> getSetmealReport() {
        Map<String, Object> map = new HashMap<>();
        //创建List集合存放套餐名
        ArrayList<String> setmealNames = new ArrayList<>();

        //查询当前已经预定的套餐即其预定数量
        List<Map<String, Object>> setmealCount = setmealDao.getSetmealReport();

        //遍历setmealCount将套餐名放入setmealNames中
        for (Map<String, Object> soMap : setmealCount) {
            String name = (String) soMap.get("name");
            setmealNames.add(name);
        }
        //将setmealNames和setmealCount放入map
        map.put("setmealNames", setmealNames);
        map.put("setmealCount", setmealCount);

        return map;
    }

    /**
     * 查询排名前四的热门套餐
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findHotSetmeal() {
        return setmealDao.findHotSetmeal();
    }
}
