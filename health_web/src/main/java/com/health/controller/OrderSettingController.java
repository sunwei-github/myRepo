package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import com.health.utils.POIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/5 14:37
 */
@Controller
@RequestMapping("ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 解析上传的文件，并将数据保存到数据库
     *
     * @param excelFile 接受的Excel文件
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> excel = POIUtil.readExcel(excelFile);
            if (excel != null && excel.size() > 0) {
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] string : excel) {
                    //获取每一条数据并转成bean
                    OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                    //将每一条bean数据放入集合
                    orderSettings.add(orderSetting);
                }
                //调用service层方法处理
                orderSettingService.add(orderSettings);
            }
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    /**
     * 查询指定月的预约设置
     *
     * @param date 指定月份
     * @return 指定月份的预约数据
     */
    @RequestMapping(value = "getOrderSettingByMonth", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 设置指定日期的最大可预约数据
     */
    @RequestMapping(value = "editNumberByDate", method = RequestMethod.POST)
    @ResponseBody
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (RuntimeException re) {
            return new Result(false, re.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
