package com.health.service;

import com.health.entity.Result;

import java.util.List;
import java.util.Map; /**
 * @author W.Sun
 * @date 2019/11/8 15:29
 */
public interface OrderService {
    /**
     * 提交预约订单
     * @param map
     * @return
     */
    Result submit(Map map) throws Exception;

    /**
     * 根据预约订单id查询预约信息
     * @param id             订单id
     * @return               预约信息
     */
    Map findById(Integer id);

    /**
     * 查询当日预约数
     * @param today_s        当日日期
     * @return               当日预约束
     */
    Integer findOrderOfToday(String today_s);

    /**
     * 查询当日到诊数
     * @param today_s        当日日期
     * @return               当日到诊数
     */
    Integer findVisitOfToday(String today_s);

    /**
     * 查询本周预约数
     * @param firstDayOfWeek_s         本周第一天日期
     * @return                         本周预约数
     */
    Integer findOrderOfThisWeek(String firstDayOfWeek_s);

    /**
     * 查询本周到诊数
     * @param firstDayOfWeek_s          本周第一天日期
     * @return                          本周到诊数
     */
    Integer findVisitOfThisWeek(String firstDayOfWeek_s);

    /**
     * 查询本月预约数
     * @param firstDay4ThisMonth_s      本月第一天日期
     * @return                          本月预约数
     */
    Integer findOrderOfThisMonth(String firstDay4ThisMonth_s);

    /**
     * 查询本月到诊数
     * @param firstDay4ThisMonth_s      本月第一天日期
     * @return                          本月到诊数
     */
    Integer findVisitOfThisMonth(String firstDay4ThisMonth_s);
}
