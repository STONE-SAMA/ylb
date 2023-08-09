package org.example.common.constants;

public class RedisKeys {

    //投资排行榜
    public static final String REDIS_KEY_INVEST_RANK = "INVEST:RANK";

    //注册时短信验证码 SMS:REG:手机号
    public static final String REDIS_KEY_SMS_CODE_REG = "SMS:REG:";

    //登录时短信验证码 SMS:LOGIN:手机号
    public static final String REDIS_KEY_SMS_CODE_LOGIN = "SMS:LOGIN:";

    //实名时短信验证码 SMS:REALNAME:手机号
    public static final String REDIS_KEY_SMS_CODE_REALNAME = "SMS:REALNAME:";

    //redis自增
    public static final String KEY_RECHARGE_ORDERID = "RECHARGE:ORDERID:SEQ";

    //订单号id
    public static final String KEY_ORDERID_SET = "RECHARGE:ORDERID:SET";

}
