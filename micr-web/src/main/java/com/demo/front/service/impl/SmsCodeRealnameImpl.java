package com.demo.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.common.constants.RedisKeys;
import org.example.common.util.SmsSendUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service("smsCodeRealnameImpl")
public class SmsCodeRealnameImpl implements SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${aliyun.sms.appcode}")
    private String myAppCode;

    @Value("${aliyun.sms.smsSignId}")
    private String smsSignId;

    @Value("${aliyun.sms.realnameTemplated}")
    private String templateId;

    @Override
    public boolean sendSms(String phone) {
        boolean send = false;
        //获取验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("实名验证码的随机数 random=" + random);

//        JSONObject jsonObject = SmsSendUtil.sendSms(myAppCode, smsSignId, templateId, phone, random);
//        if (Integer.valueOf(jsonObject.get("code").toString()) == 0) {
//            send = true;
//            //将code保存到redis
//            String key = RedisKeys.REDIS_KEY_SMS_CODE_REALNAME + phone;
//            stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
//        }

        /* 测试用，跳过短信发送 */
        send = true;
        String key = RedisKeys.REDIS_KEY_SMS_CODE_REALNAME + phone;
        stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);

        return send;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKeys.REDIS_KEY_SMS_CODE_REALNAME + phone;
        if (stringRedisTemplate.hasKey(key)) {
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)) {
                return true;
            }
        }
        return false;
    }
}
