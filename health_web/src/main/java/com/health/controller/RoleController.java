package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    /**
     * 角色分页
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增检查组
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role role , Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.add(role,permissionIds,menuIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    /**
     * 根据角色id查询权限数据
     *
     * @param roleId
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer roleId) {
        try {

            Role role = roleService.findById(roleId);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 查询角色所关联的权限
     * @param roleId
     * @return
     */
    @GetMapping("/findPermissionIdByRoleId")
    public Result findPermissionIdByRoleId(Integer roleId) {
        try {
            List<Integer> permissionIds = roleService.findPermissionIdByRoleId(roleId);
            return new Result(true, MessageConstant.QUERY_LINK_PERMISSION_SUCCESS, permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_LINK_PERMISSION_FAIL);
        }
    }

    /**
     * 查询角色所关联的菜单
     * @param roleId
     * @return
     */
    @GetMapping("/findMenuIdByRoleId")
    public Result findMenuIdByRoleId(Integer roleId) {
        try {
            List<Integer> menuIds = roleService.findMenuIdByRoleId(roleId);
            return new Result(true, MessageConstant.QUERY_LINK_MENU_SUCCESS, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_LINK_MENU_FAIL);
        }
    }

    /**
     * 编辑角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Role role , Integer[] permissionIds,Integer[] menuIds) {
        try {
            roleService.edit(role,permissionIds,menuIds);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    /**
     * 删除检查组
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            roleService.deleteByRoleId(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }

    /**
     *
     * 查询所有角色
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> roles = roleService.findAll();
            return new Result(true, MessageConstant.QUERY_ALL_ROLE_SUCCESS, roles);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ALL_ROLE_FAIL);
        }
    }
}
