package com.hood.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.hood.msmservice.service.MsmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


import java.util.Map;


@Service
public class MsmServiceImpl implements MsmService {
    //发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {

            //判定号码是否为空
            if (StringUtils.isEmpty(phone)){
                return false;
            }
            //阿里云短信服务初始化
            DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4GAny7f7cVbeJ6cZba8N", "0yC9E6Q3tQmVBNPzIvhajqQxxDjJBh");
            IAcsClient client = new DefaultAcsClient(profile);
            //设置固定的相关参数
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);   //调用了阿里云固定的方法
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");

            //设置发送相关的参数
            request.putQueryParameter("PhoneNumbers",phone);  //PhoneNumbers，阿里云固定名称
            request.putQueryParameter("SignName","旗舰的在线教育网站");  //申请的签名叫啥，写过来
            request.putQueryParameter("TemplateCode","SMS_196619374");  //申请的模板code
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));  //验证码,需要转换成json数据，依赖中fastjson可以做到

            //最终发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
