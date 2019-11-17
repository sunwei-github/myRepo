package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.CheckGroupDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;
import com.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/1 12:45
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加一个新的检查组
        checkGroupDao.add(checkGroup);
        //添加新检查组中的检查项
        Integer id = checkGroup.getId();
        setCheckGroupAndCheckItem(id, checkitemIds);
    }

    //设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    //根据检查组id查询检查组数据
    @Override
    public CheckGroup findCheckGroupById(Integer id) {
        return checkGroupDao.findCheckGroupById(id);
    }

    //查询与指定检查组相关联的检查项信息
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
            return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //更改检查组信息
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        Integer id = checkGroup.getId();
        //更改检查组信息
        checkGroupDao.edit(checkGroup);
        //删除旧的检查组与检查项关系
        checkGroupDao.deleteAssociation(id);
        //添加新的检查组与检查项关系
        for (Integer checkItemId : checkItemIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("checkgroup_id", id);
            map.put("checkitem_id", checkItemId);
            checkGroupDao.addAssociation(map);
        }
    }

    //删除指定的检查组
    @Override
    public void delete(Integer id) {
        //删除对应的套餐和检查项数据
        checkGroupDao.deleteRelationshipWithSetMeal(id);
        //删除检查组及对应的检查项的关系
        checkGroupDao.deleteAssociation(id);
        //删除检查组
        checkGroupDao.deleteCheckGroup(id);
    }

    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
