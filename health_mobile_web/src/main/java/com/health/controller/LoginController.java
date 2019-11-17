package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
/**
 * @author W.Sun
 * @date 2019/11/8 19:51
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Result login(HttpServletResponse response, @RequestBody Map map){
        String validateCode = (String)map.get("validateCode");
        String telephone = (String) map.get("telephone");
        //获取redis中的验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(StringUtils.isEmpty(code) || !code.equals(validateCode)){
            //验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码正确
        //判断用户是否为会员
        if(memberService.findMemberByTelephone(telephone) == null){
            //非会员
            Member member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(60*60*24*30);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}

