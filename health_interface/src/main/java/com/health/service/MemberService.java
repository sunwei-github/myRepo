package com.health.service;

import com.health.pojo.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/8 20:01
 */
public interface MemberService {
    /**
     * 添加会员
     */
    void add(Member member);

    /**
     * 根据电话号码查询会员
     *
     * @return
     */
    Member findMemberByTelephone(String telephone);

    /**
     * 查询今日新增会员数量
     *
     * @param today
     * @return
     */
    Integer findMemberOfToday(String today);

    /**
     * 查询所有会员数量
     *
     * @return 所有会员数量
     */
    Integer findAllMemberCount();

    /**
     * 查询本周新增会员数
     *
     * @param firstDayOfWeek 本周第一天日期
     * @return 本周新增会员数量
     */
    Integer findMemberOfThisWeek(String firstDayOfWeek);

    /**
     * 查询本月新增会员数量
     *
     * @param firstDay4ThisMonth 本月第一天日期
     * @return 本月新增会员数量
     */
    Integer findMemberOfThisMonth(String firstDay4ThisMonth);

    /**
     * 查询指定月份会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCount(ArrayList<String> months);

    /**
     * 饼状图  - 获取会员性别比例
     * @return
     */
    Map<String,Object> findMemberSex();

    /**
     * 饼状图  - 获取会员年龄比例
     * @return
     */
    Map<String,Object> findMemberAge();
}
