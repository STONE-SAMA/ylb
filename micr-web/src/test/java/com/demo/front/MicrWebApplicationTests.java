package com.demo.front;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.common.util.HttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MicrWebApplicationTests {

    @Test
    void contextLoads() {
        String s = "{\"msg\":\"成功\",\"smsid\":\"16901688754041268511283883\",\"code\":\"0\",\"balance\":\"19\"}";
        JSONObject jsonObject = JSONObject.parseObject(s);

        System.out.println(Integer.valueOf(jsonObject.get("code").toString()));
        System.out.println(Integer.valueOf(jsonObject.get("code").toString())==0);
    }

    @Test
    public void testSendCode() {
        String phone = "19157878845";

        String random = RandomStringUtils.randomNumeric(4);

        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "e666a3f985324f5e87ab702ac766539b";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
