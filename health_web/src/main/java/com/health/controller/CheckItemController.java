package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.service.CheckItemService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.health.pojo.CheckItem;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author W.Sun
 * @date 2019/10/31 9:18
 *检查项控制器
 */
@Controller
@RequestMapping("checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

    }

    @RequestMapping(value = "findPage", method = RequestMethod.POST)
    @ResponseBody
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkItemService.findPage(queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public Result deleteCheckItemById(Integer id){
        try {
            checkItemService.deleteCheckItemById(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (RuntimeException e){
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @RequestMapping(value = "findCheckItemById", method = RequestMethod.GET)
    @ResponseBody
    public CheckItem findCheckItemById(Integer id){
        CheckItem checkItem = null;
        try {
            checkItem =  checkItemService.findCheckItemById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkItem;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll(){
        try {
            List<CheckItem> checkItems = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
