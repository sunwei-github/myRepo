package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单表
 */
@RestController
@RequestMapping("/menu")
public class menuController {
    @Reference
    private MenuService menuService;

    /**
     * 菜单分页
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = menuService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu) {
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 根据菜单的id查询权限表的数据
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
            Menu menu = menuService.findById(id);
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    /**
     *
     * 查询所有菜单
     * @return
     */
    @GetMapping("/findMenuAll")
    public Result findMenuAll() {
        try {
            List<Menu> menus = menuService.findMenuAll();
            return new Result(true, MessageConstant.QUERY_ALLMENU_SUCCESS, menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ALLMENU_FAIL);
        }
    }

    /**
     * 菜单编辑
     *
     * @param menu
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Menu menu) {
        try {
            menuService.edit(menu);
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            menuService.deleteByMenuId(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }
}
