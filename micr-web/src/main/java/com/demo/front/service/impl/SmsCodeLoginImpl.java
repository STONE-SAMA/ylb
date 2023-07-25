package com.demo.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.common.constants.RedisKeys;
import org.example.common.util.HttpUtils;
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

        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "e666a3f985324f5e87ab702ac766539b";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        //验证码：**code**，**minute**分钟内有效，您正在进行注册，若非本人操作，请勿泄露。
        querys.put("mobile", phone);
        querys.put("param", "**code**:" + random + ",**minute**:3");
        //smsSignId（短信前缀）和templateId（短信模板）
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "02551a4313154fe4805794ca069d70bf");
        Map<String, String> bodys = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            if (Integer.valueOf(jsonObject.get("code").toString()) == 0) {
                send = true;
                //将code保存到redis
                String key = RedisKeys.REDIS_KEY_SMS_CODE_LOGIN + phone;
                stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
