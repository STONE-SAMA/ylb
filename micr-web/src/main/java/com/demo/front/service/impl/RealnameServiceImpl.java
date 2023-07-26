package com.demo.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.common.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RealnameServiceImpl {

    @Value("${aliyun.realname.appcode}")
    private String myAppcode;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    private UserService userService;

    public boolean handleRealname(String phone, String name, String idCard) {
        String host = "https://idcardcheck2.hzylgs.com";
        String path = "/api-mall/api/id_card_v2/check";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + myAppcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idcard", idCard);
        bodys.put("name", name);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            if (Integer.valueOf(jsonObject.get("code").toString()) == 200) {
                JSONObject object = (JSONObject) jsonObject.get("data");
                if (Integer.valueOf(object.get("result").toString()) == 0) {
                    //身份核验通过
                    //更新数据库
                    boolean modifyRes = userService.modifyRealname(phone, name, idCard);
                    return modifyRes;
                } else {
                    //身份核验未通过
                    return false;
                }
            } else {
                //请求失败
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
