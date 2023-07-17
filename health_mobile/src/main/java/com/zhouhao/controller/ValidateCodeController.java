package com.zhouhao.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@SuppressWarnings("all")
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("send4Login")
    public Result send4Login(String telephone){
        try {
            DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAI5tMtafkduQWXKDXpDXxC", "w0UBQvZD1b5P4Xcg5NF1oqGq9TTISp");
            DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);

            // 创建API请求并设置参数
            SendSmsRequest request = new SendSmsRequest();

            request.setPhoneNumbers(telephone); // 该参数值为假设值，请您根据实际情况进行填写
            request.setTemplateCode("SMS_154950909");
            request.setSignName("阿里云短信测试"); // 该参数值为假设值，请您根据实际情况进行填写
            VerifyCodeUtil verifyCodeUtil = new VerifyCodeUtil();
            String code = verifyCodeUtil.getCharArray(6);
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            Jedis jedis = jedisPool.getResource();
            jedis.set("Code_" + telephone, code);
            jedis.expire("Code_" + telephone, 60);
            System.out.println(code);
//            SendSmsResponse response = client.getAcsResponse(request);
//            System.out.println(JSON.toJSONString(response));
//             打印您需要的返回值，此处打印的是此次请求的 RequestId
//            System.out.println(response.getRequestId());
        } catch (ClientException e) {
            // 打印错误码
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
