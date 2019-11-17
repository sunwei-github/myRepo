package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.RoleDao;
import com.health.entity.PageResult;
import com.health.pojo.Role;
import com.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 16:14
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    /**
     * 通过用户id查询角色列表
     * @param id           用户id
     * @return             角色集合
     */
    @Override
    public Set<Role> findRoleByUserId(Integer id) {
        Set<Role> roleSet = roleDao.findRoleByUserId(id);
        return roleSet;
    }

    /**
     * 根据用户名查询角色
     * @param username          用户名
     * @return                  角色集合
     */
    @Override
    public List<Role> findRoleByUsername(String username) {
        return roleDao.findRoleByUsername(username);
    }

    /**
     * 根据用户名查询角色id
     * @param username                用户名
     * @return                        角色id集合
     */
    @Override
    public List<Integer> findRoleIdByUsername(String username) {
        return roleDao.findRoleIdByUsername(username);
    }

    /**
     * 角色分页
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
        Page<Role> page = roleDao.selectByCondition(queryString);
        //创建pageResult的对象，将page查询出来的放入pageResult对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //新增角色
        roleDao.add(role);
        //获取新增角色后查询到的id
        Integer roleId = role.getId();
        //关联角色和权限
        setRoleAndPermission(roleId,permissionIds);
        setRoleAndMenu(roleId,menuIds);
    }

    /**
     * 通过中间表关联角色和权限
     * @param roleId
     * @param permissionIds
     */
    public void setRoleAndPermission(Integer roleId ,Integer[] permissionIds) {
        for (Integer permissionId : permissionIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("roleId",roleId);
            map.put("permissionId",permissionId);
            roleDao.setRoleAndPermission(map);
        }
    }

    /**
     * 通过中间表关联角色和菜单
     * @param roleId
     * @param menuIds
     */
    public void setRoleAndMenu(Integer roleId ,Integer[] menuIds) {
        for (Integer menuId : menuIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("roleId",roleId);
            map.put("menuId",menuId);
            roleDao.setRoleAndMenu(map);
        }
    }

    /**
     * 查询角色所关联的权限
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> findPermissionIdByRoleId(Integer roleId) {
        return roleDao.findPermissionIdByRoleId(roleId);
    }

    /**
     * 查询角色所关联的菜单
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> findMenuIdByRoleId(Integer roleId) {
        return roleDao.findMenuIdByRoleId(roleId);
    }

    /**
     * 根据角色id查询权限数据
     *
     * @param roleId
     * @return
     */
    @Override
    public Role findById(Integer roleId) {
        return roleDao.findById(roleId);
    }

    /**
     * 编辑角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //编辑角色的表
        roleDao.editRole(role);

        //删除之前角色和权限的关联
        roleDao.deleteT_role_permissionByRoleId(role.getId());

        //通过中间表关联角色和权限
        setRoleAndPermission(role.getId(),permissionIds);

        //删除之前角色和菜单的关联
        roleDao.deleteT_role_menuByRoleId(role.getId());

        //通过中间表关联角色和菜单
        setRoleAndMenu(role.getId(),menuIds);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Override
    public void deleteByRoleId(Integer roleId) {
        //查询用户和角色关联表判断用户中是否有角色
        Integer count = roleDao.findUserCountByRoleId(roleId);
        if (count != 0) {
            //有关联不能删除
            throw new RuntimeException("此角色已经被用户关联，不能删除");
        }
        //删除角色和权限之间的关联，其实就是删除t_role_permission表中有当前id的数据
        roleDao.deleteT_role_permissionByRoleId(roleId);
        //删除角色和菜单之间的关联，其实就是删除t_role_menu表中有当前id的数据
        roleDao.deleteT_role_menuByRoleId(roleId);
        //走到这里可以删除
        roleDao.deleteRoleById(roleId);

    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
