package com.health.dao;

import com.health.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author W.Sun
 * @date 2019/11/8 16:08
 */
public interface MemberDao {
    /**
     * 根据电话号码查询会员信息
     *
     * @param telephone 电话号码
     * @return 会员信息
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员数据
     *
     * @param member 会员数据
     */
    void add(Member member);

    /**
     * 查询当日新增会员数量
     *
     * @param today 当日日期
     * @return 新增会员数量
     */
    Integer findMemberOfToday(String today);

    /**
     * 查询所有会员数量
     *
     * @return 所有会员数量
     */
    Integer findAllMemberCount();

    /**
     * 查询本周新增会员数量
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
     * 查询指定月会员数量
     * @param month
     * @return
     */
    int findMemberCount(String month);

    /**
     * 查询会员性别比例
     * @return
     */

    List<Map<String, Integer>> findMemberSexCount();

    /**
     * 查询会员年龄比例
     * @return
     */
    List<Map<String,Integer>> findMemberAge();
}
