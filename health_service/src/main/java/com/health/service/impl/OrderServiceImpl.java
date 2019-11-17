package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.constant.MessageConstant;
import com.health.dao.MemberDao;
import com.health.dao.OrderDao;
import com.health.dao.OrderSettingDao;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.pojo.OrderSetting;
import com.health.service.OrderService;
import com.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/8 15:30
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;
    /**
     * 提交预约订单
     * @param map               预约信息
     * @return                  预约结果
     */
    @Override
    public Result submit(Map map) throws Exception {
        //1.判断所选日期是否设置预约
        String orderDate = (String) map.get("orderDate");
        //转成Date类型
        Date date = DateUtils.parseString2Date(orderDate);
        if(orderSettingDao.findCountByOrderDate(date) < 1){
            //表示当天没有预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.判断当日预约名额是否已满
        //获取预约日期的预约设置
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(date);
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(number <= reservations){
            //表示预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.判断当前用户是否是会员
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        if(member != null){
            //是会员，避免重复预约
            Integer id = member.getId();
            Integer setmealId = (Integer) map.get("setmealId");
            Order order = new Order(id, date, null, null, setmealId);
            List<Order> orderList = orderDao.findByCondition(order);
            if(orderList != null && orderList.size()>0){
                //表示该用户已经在同一天预约了同一个套餐
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //不是会员，添加到会员表
             member = new Member();
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //进行预约
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    /**
     * 根据预约订单id查询预约信息
     * @param id             订单id
     * @return               预约信息
     */
    @Override
    public Map findById(Integer id) {
        return orderDao.findById(id);
    }

    /**
     * 查询当日预约数
     * @param today_s        当日日期
     * @return               当日预约束
     */
    @Override
    public Integer findOrderOfToday(String today_s) {
        return orderDao.findOrderOfToday(today_s);
    }

    /**
     * 查询当日到诊数
     * @param today_s        当日日期
     * @return               当日到诊数
     */
    @Override
    public Integer findVisitOfToday(String today_s) {
        return orderDao.findVisitOfToday(today_s);
    }

    /**
     * 查询本周预约数
     * @param firstDayOfWeek_s         本周第一天日期
     * @return                         本周预约数
     */
    @Override
    public Integer findOrderOfThisWeek(String firstDayOfWeek_s) {
        return orderDao.findOrderOfThisWeek(firstDayOfWeek_s);
    }

    /**
     * 查询本周到诊数
     * @param firstDayOfWeek_s          本周第一天日期
     * @return                          本周到诊数
     */
    @Override
    public Integer findVisitOfThisWeek(String firstDayOfWeek_s) {
        return orderDao.findVisitOfThisWeek(firstDayOfWeek_s);
    }

    /**
     * 查询本月预约数
     * @param firstDay4ThisMonth_s      本月第一天日期
     * @return                          本月预约数
     */
    @Override
    public Integer findOrderOfThisMonth(String firstDay4ThisMonth_s) {
        return orderDao.findOrderOfThisMonth(firstDay4ThisMonth_s);
    }

    /**
     * 查询本月到诊数
     * @param firstDay4ThisMonth_s      本月第一天日期
     * @return                          本月到诊数
     */
    @Override
    public Integer findVisitOfThisMonth(String firstDay4ThisMonth_s) {
        return orderDao.findVisitOfThisMonth(firstDay4ThisMonth_s);
    }
}
