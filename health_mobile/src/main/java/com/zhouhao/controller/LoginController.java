package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.Member;
import com.zhouhao.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("check")
    public Result check(HttpServletResponse response, @RequestBody Map<String, String> loginInfo){
        Jedis jedis = jedisPool.getResource();
        String telephone = loginInfo.get("telephone");
        String code = jedis.get("Code_" + telephone);

        if(code.equalsIgnoreCase(loginInfo.get("validateCode"))){
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            Cookie cookie = new Cookie(telephone, "ILoveU");
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);

            String jsonString = JSON.toJSONString(member);
            jedis.setex(telephone, 60*30, jsonString);

            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }

        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }
}
