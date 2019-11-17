package com.health.dao;

import com.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/5 15:32
 */
public interface OrderSettingDao {

    /**
     * 根据日期查找数据
     *
     * @param orderDate 预约日期
     * @return 数据记录数量
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 编辑预约数据
     *
     * @param orderSetting 预约数据
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 添加预约数据
     *
     * @param orderSetting 预约数据
     */
    void addOrderSetting(OrderSetting orderSetting);

    /**
     * 查询指定月的预约设置
     * @param map            指定月的起始日期，结束日期
     * @return               指定月的预约设置
     */
    List<OrderSetting> findOrderSettingByMonth(Map<String, String> map);

    /**
     * 查询指定日期的已预约人数
     * @param orderDate              预约数据
     * @return                       已预约人数
     */
    long findReservationsByOrderDate(Date orderDate);

    /**
     * 根据预约日期查找预约设置
     * @param orderDate         预约日期
     */
    OrderSetting findOrderSettingByOrderDate(Date orderDate);

    /**
     * 根据预约日期id修改已预约人数
     * @param orderSetting              预约设置数据
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
