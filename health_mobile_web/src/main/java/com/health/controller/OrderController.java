package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.health.service.OrderService;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Order;
import com.health.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.FacesRequestAttributes;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/8 15:04
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     */
    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    public Result submit(@RequestBody Map map){
        //验证验证码是否正确
        //从redis中获取验证码
        String telephone = (String)map.get("telephone");
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取前端传输的验证码
        String validateCode = (String)map.get("validateCode");
        //比较验证码是否正确
        if(StringUtils.isEmpty(code) || !code.equals(validateCode)){
            //验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //调用service方法，进行预约
        Result result;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_Fail);
        }

        //预约成功，发送短信通知
        if(result.isFlag()){
            String orderDate = (String) map.get("orderDate");
            System.out.println("已预约成功，请在 " + orderDate + "前往检查");
            /*try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }*/
        }
        return result;
    }

    /**
     * 根据id查询预约信息
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map orderMap = orderService.findById(id);
            Date orderDate = (Date)orderMap.get("orderDate");
            String[] split = orderDate.toString().split(" ");
            orderMap.put("orderDate", split[0]);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
