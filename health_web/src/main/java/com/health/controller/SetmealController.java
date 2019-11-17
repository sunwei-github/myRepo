package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import com.health.utils.QiniuUtil;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author W.Sun
 * @date 2019/11/3 14:58
 */
@Controller
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "findPage", method = RequestMethod.POST)
    @ResponseBody
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            return setmealService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        //获取文件名
        String originalFilename = imgFile.getOriginalFilename();
        //获取文件后缀名
        String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
        //获取一个独一无二的文件名
        String newFileName = UUID.randomUUID().toString() + "." + filenameExtension;
        try {
            //上传图片到七牛云
            QiniuUtil.upload2Qiniu(imgFile.getBytes(), newFileName);
            //将上传到七牛云的图片信息存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, newFileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal, checkgroupIds);
            if(setmeal.getImg()!=null){
                //将存到数据库的图片信息存到redis
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
            }
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping(value = "findSetmealById", method = RequestMethod.GET)
    @ResponseBody
    public Result findSetmealById(Integer id){
        try {
            Setmeal setmeal =  setmealService.findSetmealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping(value = "findCheckgroupIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> findCheckgroupIds(Integer id){
        try {
            List<Integer> checkgroupIds = setmealService.findCheckgroupIds(id);
            return checkgroupIds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

}
