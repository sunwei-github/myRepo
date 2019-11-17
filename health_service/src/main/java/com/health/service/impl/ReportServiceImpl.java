package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.service.MemberService;
import com.health.service.OrderService;
import com.health.service.ReportService;
import com.health.service.SetmealService;
import com.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/12 15:49
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String, Object> map = new HashMap<>();
        //当前日期：reportDate
        String reportDate = DateUtils.parseDate2String(new Date());
        //本日新增会员数:todayNewMember
        Date today = DateUtils.getToday();
        String today_S = DateUtils.parseDate2String(today);
        Integer todayNewMember = memberService.findMemberOfToday(today_S);

        //总会员数:totalMember
        Integer totalMember = memberService.findAllMemberCount();

        //本周第一天日期
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(new Date());
        String firstDayOfWeek_S = DateUtils.parseDate2String(firstDayOfWeek);
        //本周新增会员数
        Integer thisWeekNewMember = memberService.findMemberOfThisWeek(firstDayOfWeek_S);


        //本月第一天日期
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        String firstDay4ThisMonth_S = DateUtils.parseDate2String(firstDay4ThisMonth);
        //本月新增会员数
        Integer thisMonthNewMember = memberService.findMemberOfThisMonth(firstDay4ThisMonth_S);

        //本日预约数量
        Integer todayOrderNumber = orderService.findOrderOfToday(today_S);
        //本日到诊数量
        Integer todayVisitsNumber = orderService.findVisitOfToday(today_S);

        //本周预约数量
        Integer thisWeekOrderNumber = orderService.findOrderOfThisWeek(firstDayOfWeek_S);
        //本周到诊数量
        Integer thisWeekVisitsNumber = orderService.findVisitOfThisWeek(firstDayOfWeek_S);

        //本月预约数量
        Integer thisMonthOrderNumber = orderService.findOrderOfThisMonth(firstDay4ThisMonth_S);
        //本月到诊数量
        Integer thisMonthVisitsNumber = orderService.findVisitOfThisMonth(firstDay4ThisMonth_S);

        //查询排名前四的人气套餐
        List<Map<String, Object>> hotSetmeal = setmealService.findHotSetmeal();

        map.put("reportDate", reportDate);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrderNumber);
        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        map.put("hotSetmeal", hotSetmeal);
        return map;
    }
}
