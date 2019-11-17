package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/11 12:51
 */
@RequestMapping("/user")
@RestController
public class UserController {
    @Reference
    private UserService userService;

    /**
     * 获取当前登录用户的用户名
     */
    @RequestMapping(value = "/findUsername", method = RequestMethod.GET)
    public Result findUserByUsername(){
        try {
            //从SpringSecurity框架中获取容器，用户登录信息就放在容器中
            SecurityContext context = SecurityContextHolder.getContext();
            //从容器中获取认证对象
            Authentication authentication = context.getAuthentication();
            //通过认证对象获取用户对象
            User user = (User) authentication.getPrincipal();
            //将user对象的username返回
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 获取当前登录用户的菜单
     * @param username         当前登录用户名
     * @return
     */
    @RequestMapping(value = "/findMenu", method = RequestMethod.GET)
    public Result findMenu(String username){
        try {
            List<Map> menuList = userService.findMenu(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 用户分页
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = userService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增用户
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody com.health.pojo.User user , Integer[] roleIds) {
        try {
            userService.add(user,roleIds);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    /**
     * 根据用户id查询用户数据
     *
     * @param userId
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer userId) {
        try {
            com.health.pojo.User user = userService.findById(userId);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 查询用户所关联的角色
     * @param userId
     * @return
     */
    @GetMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(Integer userId) {
        try {
            List<Integer> roleIds = userService.findRoleIdsByUserId(userId);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 编辑用户
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody com.health.pojo.User user , Integer[] roleIds) {
        try {
            userService.edit(user,roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            userService.deleteByUserId(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }
}
