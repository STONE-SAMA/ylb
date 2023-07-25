package com.demo.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.front.config.SmsConfig;
import com.demo.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

/**
 * 发送注册短信验证码
 */
@Service(value = "smsCodeRegisterImpl")
public class SmsCodeRegisterImpl implements SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @Resource
//    private SmsConfig smsConfig;

    @Value("${aliyun.sms.appcode}")
    private String myAppCode;

    @Value("${aliyun.sms.smsSignId}")
    private String smsSignId;

    @Value("${aliyun.sms.registerTemplated}")
    private String templateId;


    /**
     * 发送短信
     * @param phone
     * @return
     */
    @Override
    public boolean sendSms(String phone) {
        boolean send = false;
        //获取验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("注册验证码的随机数 random=" + random);
//        //更新content中的%s
//        String content = String.format(smsConfig.getContent(), random);
//        //使用HttpClient发送get请求给第三方
//        CloseableHttpClient client = HttpClients.createDefault();
//        String url = smsConfig.getUrl() + "";
//        HttpGet get = new HttpGet(smsConfig.getUrl());

        JSONObject jsonObject = SmsSendUtil.sendSms(myAppCode, smsSignId, templateId, phone, random);
        if (Integer.valueOf(jsonObject.get("code").toString()) == 0) {
            send = true;
            //将code保存到redis
            String key = RedisKeys.REDIS_KEY_SMS_CODE_REG + phone;
            stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
        }

        return send;
    }

    /**
     * 验证提交的验证码是否正确
     * @param phone
     * @param code 提交的验证码
     * @return
     */
    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKeys.REDIS_KEY_SMS_CODE_REG + phone;
        if (stringRedisTemplate.hasKey(key)){
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)){
                return true;
            }
        }
        return false;
    }
}
