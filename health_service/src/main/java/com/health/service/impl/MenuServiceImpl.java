package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.MenuDao;
import com.health.entity.PageResult;
import com.health.pojo.Menu;
import com.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/12 8:13
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 跟据角色id查询菜单
     * @param ids          角色id字符串
     * @return             菜单
     */
    @Override
    public List<Map> findMenu(String ids) {
        return menuDao.findMenu(ids);
    }

    /**
     * 查询所有可访问的菜单
     * @param id             角色id
     * @return
     */
    @Override
    public LinkedHashSet<Menu> findAllMenu(Integer id) {
        return menuDao.findAllMenu(id);
    }

    /**
     * 菜单分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //紧跟着分页参数代码 需要分页的查询语句 （中间不能有任何代码）
        Page<Menu> page = menuDao.selectByCondition(queryString);
        //创建pageResult的对象，将page查询出来的放入pageResult对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> findMenuAll() {
        return menuDao.findMenuAll();
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    /**
     * 根据菜单的id查询权限表的数据
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 菜单编辑
     *
     * @param menu
     * @return
     */
    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void deleteByMenuId(Integer id) {
        //通过中间表t_role_menu判断t_role角色表和t_menu菜单表是否有关联
        Integer count = menuDao.findCountByMenuId(id);
        //判断count从而判断是否关联
        if (count > 0) {
            //有关联，不能删除
            throw new RuntimeException("此菜单已经关联角色，不能删除");
        }
        //没有关联，可以删除
        menuDao.deleteByPermissionId(id);
    }
}
