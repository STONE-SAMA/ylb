package com.demo.front.service;

public interface SmsService {

    /**
     * 发送短信
     *
     * @param phone
     * @return true:发送成功  false:发送失败
     */
    boolean sendSms(String phone);

    /**
     * 验证提交的验证码是否正确
     * @param phone
     * @param code 提交的验证码
     * @return
     */
    boolean checkSmsCode(String phone, String code);

}
