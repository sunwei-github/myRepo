package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.dao.MemberDao;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author W.Sun
 * @date 2019/11/8 20:01
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 添加会员
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 根据电话号码查询会员
     *
     * @return
     */
    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 查询当日新增会员数量
     *
     * @param today 当日
     * @return 新增会员数量
     */
    @Override
    public Integer findMemberOfToday(String today) {
        return memberDao.findMemberOfToday(today);
    }

    /**
     * 查询所有会员数量
     *
     * @return 所有会员数量
     */
    @Override
    public Integer findAllMemberCount() {
        return memberDao.findAllMemberCount();
    }

    /**
     * 查询本周新增会员数量
     *
     * @param firstDayOfWeek 本周第一天日期
     * @return 本周新增会员数量
     */
    @Override
    public Integer findMemberOfThisWeek(String firstDayOfWeek) {
        return memberDao.findMemberOfThisWeek(firstDayOfWeek);
    }

    /**
     * 查询本月新增会员数量
     *
     * @param firstDay4ThisMonth 本月第一天日期
     * @return 本月新增会员数量
     */
    @Override
    public Integer findMemberOfThisMonth(String firstDay4ThisMonth) {
        return memberDao.findMemberOfThisMonth(firstDay4ThisMonth);
    }

    /**
     * 查询指定月会员数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCount(ArrayList<String> months) {
        //根据月份统计会员数量
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            month=month+"-31";
            int count=memberDao.findMemberCount(month);
            list.add(count);
        }
        return list;
    }

    /**
     * 获取会员性别比例
     *
     * @return
     */
    @Override
    public Map<String, Object> findMemberSex() {
        Map<String, Object> rsMap = new HashMap<>();//业务层返回结果
        List<String> memberSex = new ArrayList<>();//性别集合
        List<Map<String, Integer>> memberSexCount = memberDao.findMemberSexCount();//性别比例  男  8个
        if (memberSexCount != null || memberSexCount.size() > 0) {
            for (Map map : memberSexCount) {
                //获取性别
                String sex = (String) map.get("name");
                memberSex.add(sex);
            }
        }
        rsMap.put("memberSex", memberSex);
        //性别比例
        rsMap.put("memberSexCount", memberSexCount);
        return rsMap;
    }

    /**
     * 获取会员年龄比例
     * @return
     */
    @Override
    public Map<String, Object> findMemberAge() {
        Map<String, Object> rsMap = new HashMap<>();//业务层返回结果
        List<String> memberAge = new ArrayList<>();//年龄段名称集合
        List<Map<String, Integer>> memberAgeCount = memberDao.findMemberAge();//不同年龄段的年龄数据
        //遍历年龄数据，取出不同年龄段的名称
        for (Map map : memberAgeCount) {
            //获取年龄段名称
            String name = (String) map.get("name");
            //添加到memberAge
            memberAge.add(name);
        }
        rsMap.put("memberAge", memberAge);
        //年龄比例
        rsMap.put("memberAgeCount", memberAgeCount);
        return rsMap;
    }
}
