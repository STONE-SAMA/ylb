package org.example.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.common.constants.RedisKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SmsSendUtil {

    /**
     * 发送短信
     * @param myAppCode
     * @param smsSignId 短信前缀 模板
     * @param templateId 短信内容模板
     * @param phone
     * @param random 验证码
     * @return
     */
    public static JSONObject sendSms(String myAppCode, String smsSignId, String templateId, String phone, String random){
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + myAppCode);
        Map<String, String> querys = new HashMap<>();
        //验证码: 如:**code**，**minute**分钟内有效，若非本人操作，请勿泄露。
        querys.put("mobile", phone);
        querys.put("param", "**code**:" + random + ",**minute**:3");
        //smsSignId（短信前缀）和templateId（短信模板）
        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            return JSONObject.parseObject(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
