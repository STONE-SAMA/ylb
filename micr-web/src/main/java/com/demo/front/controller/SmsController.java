package com.demo.front.controller;

import com.demo.front.service.SmsService;
import com.demo.front.view.RespResult;
import io.swagger.annotations.Api;
import org.example.common.constants.Constants;
import org.example.common.constants.RedisKeys;
import org.example.common.util.CommonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1/sms")
public class SmsController extends BaseController {

    @Resource(name = "smsCodeRegisterImpl")
    private SmsService smsService;

    @Resource(name = "smsCodeLoginImpl")
    private SmsService loginSmsService;

    @Resource(name = "smsCodeRealnameImpl")
    private SmsService realnameSmsService;


    //发送注册验证码短信
    @GetMapping("/code/register")
    public RespResult sendCodeRegister(@RequestParam String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
//            String key = RedisKeys.REDIS_KEY_SMS_CODE_REG + phone;
//            if (stringRedisTemplate.hasKey(key)){
//
//            }
            boolean sendResult = smsService.sendSms(phone);
            if (sendResult) {
                result.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                result.setMsg("注册短信成功发送");
            } else {
                result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                result.setMsg("注册短信发送失败");
            }
        } else {
            result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            result.setMsg("电话格式错误");
        }
        return result;
    }

    //发送登录验证码
    @GetMapping("/code/login")
    public RespResult sendCodeLogin(@RequestParam String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            boolean sendResult = loginSmsService.sendSms(phone);
            if (sendResult) {
                result.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                result.setMsg("登录短信成功发送");
            } else {
                result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                result.setMsg("登录短信发送失败");
            }
        } else {
            result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            result.setMsg("电话格式错误");
        }
        return result;
    }

    //发送实名验证码
    @GetMapping("/code/realname")
    public RespResult sendCodeRealname(@RequestParam String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            boolean sendResult = realnameSmsService.sendSms(phone);
            if (sendResult) {
                result.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                result.setMsg("实名短信成功发送");
            } else {
                result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                result.setMsg("实名短信发送失败");
            }
        } else {
            result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            result.setMsg("电话格式错误");
        }
        return result;
    }
}
