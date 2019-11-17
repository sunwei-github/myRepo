package com.health.service;

import com.health.pojo.OrderSetting;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/5 15:14
 */
public interface OrderSettingService {
    /**
     * 添加可预约人数及预约日期
     */
    void add(List<OrderSetting> orderSettings);

    /**
     * 获取指定月份的预约设置
     * @param date       指定月份
     * @return           预约设置
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 设置指定日期的最大可预约人数
     * @param orderSetting         预约数据
     */
    void editNumberByDate(OrderSetting orderSetting);

    /**
     * 查询指定日期的预约设置
     */
    OrderSetting findOrderSettingByOrderDate(Date orderDate);
}
