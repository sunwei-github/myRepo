package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.OrderSettingDao;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author W.Sun
 * @date 2019/11/5 15:19
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 通过上传文件添加可预约人数和预约日期
     *
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        if (orderSettings != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {
                //检查数据库中是否已有该日的数据
                int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已有数据，则修改
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //没有数据，则添加
                    orderSettingDao.addOrderSetting(orderSetting);
                }
            }
        }
    }

    /**
     * 获取指定月份的预约参数
     *
     * @param date 指定月份
     * @return 预约参数
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //从1号开始
        String beginDate = date + "-1";
        //31号结束
        String endDate = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        //从数据库中取出指定月数据
        List<OrderSetting> orderSettings = orderSettingDao.findOrderSettingByMonth(map);
        //创建集合用来存储要返回给前台的数据
        List<Map> list = new ArrayList<>();
        //循环将查出来的数据封装入返回的集合中
        for (OrderSetting orderSetting : orderSettings) {
            Map<String, Integer> orderSettingMap = new HashMap<>();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number", orderSetting.getNumber());
            orderSettingMap.put("reservations", orderSetting.getReservations());
            //将Map装入集合
            list.add(orderSettingMap);
        }
        return list;
    }

    /**
     * 设置指定日期的最大可预约人数
     * @param orderSetting         预约数据
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询该天数据是否已存在
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //若已存在，重新设置该天最大可预约人数，且新设置的最大可预约人数不得少于已经预约的人数
            //查找该天已预约人数
            long reservations = orderSettingDao.findReservationsByOrderDate(orderSetting.getOrderDate());
            if(reservations > orderSetting.getNumber()){
                //若已预约人数多于最大预约人数
                throw new RuntimeException("已预约人数已经超过最大可预约人数！");
            }
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //若不存在，则添加
            orderSettingDao.addOrderSetting(orderSetting);
        }
    }

    @Override
    public OrderSetting findOrderSettingByOrderDate(Date orderDate) {
        return orderSettingDao.findOrderSettingByOrderDate(orderDate);
    }
}

