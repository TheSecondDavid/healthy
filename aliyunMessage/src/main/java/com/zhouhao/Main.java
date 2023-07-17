package com.zhouhao;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

public class Main {
    public static void main(String[] args) {
        try {
            DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAI5tMtafkduQWXKDXpDXxC", "w0UBQvZD1b5P4Xcg5NF1oqGq9TTISp");
            DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);

            // 创建API请求并设置参数
            SendSmsRequest request = new SendSmsRequest();

            request.setPhoneNumbers("18846178317"); // 该参数值为假设值，请您根据实际情况进行填写
            request.setTemplateCode("SMS_154950909");
            request.setSignName("阿里云短信测试"); // 该参数值为假设值，请您根据实际情况进行填写
            request.setTemplateParam("{\"code\":\"1234\"}");

            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
            // 打印您需要的返回值，此处打印的是此次请求的 RequestId
            System.out.println(response.getRequestId());
        } catch (ClientException e) {
            // 打印错误码
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }
}
