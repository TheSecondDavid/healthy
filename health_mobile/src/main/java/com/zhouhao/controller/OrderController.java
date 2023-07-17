package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.Member;
import com.zhouhao.pojo.Order;
import com.zhouhao.service.OrderService;
import com.zhouhao.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("all")
@RequestMapping("order")
@RestController
public class OrderController {
    @Reference
    OrderService orderService;
    @Autowired
    JedisPool jedisPool;
    @Reference
    OrderSettingService orderSettingService;
    @RequestMapping
    public Result Order(@RequestBody Map<String, String> map) throws ParseException {
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        //校验手机验证码
        //调用体检预约服务
        //预约失败
        //预约成功，发送短信通知

        /**
        1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        5、预约成功，更新当日的已预约人数
         */
        Jedis jedis = jedisPool.getResource();
        String code = jedis.get("Code_" + map.get("phoneNumber"));
        if(code != null && code.equalsIgnoreCase(map.get("validateCode"))){
            Member member = new Member();
            member.setRegTime(new Date());
            member.setName(map.get("name"));
            member.setSex(map.get("sex"));
            member.setPhoneNumber(map.get("phoneNumber"));
            member.setIdCard(map.get("idCard"));
            Integer id = orderService.Order(member);

            Order order = new Order(id);
            order.setMemberId(id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            order.setOrderDate(sdf.parse(map.get("orderDate")));
            order.setOrderType("微信预约");
            order.setOrderStatus("未到诊");
            order.setSetmealId(Integer.parseInt(map.get("setmealId")));

            Integer number = orderSettingService.getOrderByDay(sdf.parse(map.get("orderDate")));
            if(number <= 0){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            Integer realId = orderService.realOrder(order);
            if(id !=  null && realId == 1) {
                Order order1 = orderService.findByYuyue(map.get("orderDate"), id);
                return new Result(true, MessageConstant.ORDER_SUCCESS, order1);
            }
            if(realId == -1){
                return new Result(false, "不可重复预约");
            }
        }
        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }

    @RequestMapping("findById")
    public Result findById(String id){
        Map<String, String> map = orderService.findById(id);
        return new Result(true, null, map);
    }
}
