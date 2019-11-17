package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.PermissionDao;
import com.health.entity.PageResult;
import com.health.pojo.Permission;
import com.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:13
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 通过角色id查询权限列表
     * @param id            角色id
     * @return              权限集合
     */
    @Override
    public Set<Permission> findPermissionByRoleId(Integer id) {
        return permissionDao.findPermissionByRoleId(id);
    }

    /**
     * 权限分页
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
        Page<Permission> page = permissionDao.selectByCondition(queryString);
        //创建pageResult的对象，将page查询出来的放入pageResult对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增权限
     *
     * @param permission
     * @return
     */
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 根据权限的id查询权限表的数据 CV
     *
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        Permission permission = permissionDao.findById(id);
        return permission;
    }

    /**
     * 权限编辑
     *
     * @param permission
     * @return
     */
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @Override
    public void deleteByPermissionId(Integer id) {
        //通过中间表t_role_permission判断t_role角色表和t_permission权限表是否有关联
        Integer count = permissionDao.findCountByPermissionId(id);
        //判断count从而判断是否关联
        if (count > 0) {
            //有关联，不能删除
            throw new RuntimeException("此权限已经关联角色，不能删除");
        }
        //没有关联，可以删除
        permissionDao.deleteByPermissionId(id);
    }

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<Permission> findPermissionAll() {
        return permissionDao.findPermissionAll();
    }
}
