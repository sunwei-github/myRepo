package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Permission;
import com.health.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 权限分页
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = permissionService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增权限
     *
     * @param permission
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

    /**
     * 根据权限的id查询权限表的数据 CV
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 权限编辑
     *
     * @param permission
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.edit(permission);
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            permissionService.deleteByPermissionId(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    /**
     *
     * 查询所有权限
     * @return
     */
    @GetMapping("/findPermissionAll")
    public Result findPermissionAll() {
        try {
            List<Permission> permissions = permissionService.findPermissionAll();
            return new Result(true, MessageConstant.QUERY_ALLPERMISSION_SUCCESS, permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ALLPERMISSION_FAIL);
        }
    }
}
