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

    @Resource
    private SmsConfig smsConfig;

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
        querys.put("templateId", "a09602b817fd47e59e7c6e603d3f088d");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            if (Integer.valueOf(jsonObject.get("code").toString()) == 0) {
                send = true;
                //将code保存到redis
                String key = RedisKeys.REDIS_KEY_SMS_CODE_REG + phone;
                stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
