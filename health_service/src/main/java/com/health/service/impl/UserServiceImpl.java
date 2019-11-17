package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.UserDao;
import com.health.entity.PageResult;
import com.health.pojo.User;
import com.health.service.MenuService;
import com.health.service.RoleService;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author W.Sun
 * @date 2019/11/10 16:12
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Reference
    private RoleService roleService;

    @Reference
    private MenuService menuService;

    /**
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    /**
     * 查询当前登录用户的菜单信息
     *
     * @param username   登录用户的用户名
     * @return           登录用户的菜单
     */
    @Override
    public List<Map> findMenu(String username) {
        //根据用户名称查询对应角色的id集合
        List<Integer> roleIdList = roleService.findRoleIdByUsername(username);

        //将id集合转为String，方便SQL查询
        String roleIds = "";
        for (int i = 0; i < roleIdList.size(); i++) {
            if(i == roleIdList.size()-1){
                roleIds += roleIdList.get(i);
            } else {
                roleIds = roleIds + roleIdList.get(i) + ",";
            }
        }
        //根据角色查询对应的菜单
        List<Map> allMenuList = menuService.findMenu(roleIds);
        return allMenuList;
    }

    /*@Override
    public List<Menu> findMenu(String username) {
        List<Menu> allMenuList = new ArrayList<>();
        //根据用户名称查询对应角色
        List<Role> roleList = roleService.findRoleByUsername(username);
        //根据角色查询对应的菜单
        for (Role role : roleList) {
            LinkedHashSet<Menu> menus = menuService.findAllMenu(role.getId());
            allMenuList.addAll(menus);
        }
        //剔除掉重复的菜单
        for (int i = 0; i < allMenuList.size()-1; i++) {
            Menu posMenu = allMenuList.get(i);
            for (int j = i+1; j < allMenuList.size(); j++){
                Menu sufMenu = allMenuList.get(j);
                if(posMenu.getId()==sufMenu.getId()){
                    allMenuList.remove(j);
                    j--;
                }
            }
        }
        //重新组织二层菜单
        List<Menu> fatherMenuList = new ArrayList<>();
        //找出父菜单
        for (Menu menu : allMenuList) {
            if(menu.getParentMenuId()==null){
                fatherMenuList.add(menu);
            }
        }
        //查找父菜单对应的子菜单
        for (Menu menu : fatherMenuList) {
            for (Menu childMenu : allMenuList) {
                if(menu.getId()==childMenu.getParentMenuId()){
                    menu.getChildren().add(childMenu);
                }
            }
        }
        return fatherMenuList;
    }*/

    /**
     * 用户分页
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
        Page<User> page = userDao.selectByCondition(queryString);
        //创建pageResult的对象，将page查询出来的放入pageResult对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增用户
     * @param user
     * @param roleIds
     * @return
     */
    @Override
    public void add(User user, Integer[] roleIds) {
        //密码加密
        String password = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        //新增用户
        userDao.add(user);
        //获取新增用户后查询到的id
        Integer userId = user.getId();
        //关联用户和角色
        setUserAndRole(userId,roleIds);
    }

    /**
     * 通过中间表关联用户和角色
     * @param userId
     * @param roleIds
     */
    public void setUserAndRole(Integer userId ,Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("userId",userId);
            map.put("roleId",roleId);
            userDao.setUserAndRole(map);
        }
    }

    /**
     * 根据用户id查询用户数据
     * @param userId
     * @return
     */
    @Override
    public User findById(Integer userId) {
        return userDao.findById(userId);
    }

    /**
     * 查询用户所关联的角色
     * @param userId
     * @return
     */
    @Override
    public List<Integer> findRoleIdsByUserId(Integer userId) {
        return userDao.findRoleIdsByUserId(userId);

    }

    /**
     * 编辑用户
     * @param user
     * @param roleIds
     * @return
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //编辑用户的表
        userDao.editUser(user);

        //删除之前用户和角色的关联
        userDao.deleteT_user_roleByRoleId(user.getId());

        //通过中间表关联用户和角色
        setUserAndRole(user.getId(),roleIds);
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public void deleteByUserId(Integer id) {
        //删除用户和角色之间的关联
        userDao.deleteT_user_roleByRoleId(id);
        //删除用户
        userDao.deleteUserById(id);
    }
}
