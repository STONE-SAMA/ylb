package com.demo.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.common.constants.RedisKeys;
import org.example.common.util.HttpUtils;
import org.example.common.util.SmsSendUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("smsCodeLoginImpl")
public class SmsCodeLoginImpl implements SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${aliyun.sms.appcode}")
    private String myAppCode;

    @Value("${aliyun.sms.smsSignId}")
    private String smsSignId;

    @Value("${aliyun.sms.loginTemplateId}")
    private String templateId;


    /**
     * 发送登录验证码
     *
     * @param phone
     * @return
     */
    @Override
    public boolean sendSms(String phone) {
        boolean send = false;
        //获取验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("注册验证码的随机数 random=" + random);

        JSONObject jsonObject = SmsSendUtil.sendSms(myAppCode, smsSignId, templateId, phone, random);
        if (Integer.valueOf(jsonObject.get("code").toString()) == 0) {
            send = true;
            //将code保存到redis
            String key = RedisKeys.REDIS_KEY_SMS_CODE_LOGIN + phone;
            stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
        }

        return send;
    }

    /**
     * 检测登录验证码是否正确
     *
     * @param phone
     * @param code  提交的验证码
     * @return
     */
    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKeys.REDIS_KEY_SMS_CODE_LOGIN + phone;
        if (stringRedisTemplate.hasKey(key)) {
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)) {
                return true;
            }
        }
        return false;
    }
}
