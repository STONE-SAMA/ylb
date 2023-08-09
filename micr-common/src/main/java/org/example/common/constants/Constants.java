package org.example.common.constants;

public class Constants {
    public static final int RETURN_OBJECT_CODE_SUCCESS=1;//成功
    public static final int RETURN_OBJECT_CODE_FAIL=0;//失败

    public static final int RETURN_OBJECT_CODE_TOKEN_FAIL=3000;//TOKEN失效

    /*****产品类型*********/
    //新手宝
    public static final int PRODUCT_TYPE_XINSHOUBAO =  0;
    //优选
    public static final int PRODUCT_TYPE_YOUXUAN = 1;
    //散标
    public static final int PRODUCT_TYPE_SANBIAO = 2;


    /*****产品状态*********/
    public static final int PRODUCT_STATUS_SELLING = 0;//销售中
    public static final int PRODUCT_STATUS_SOLDOUT = 1;//售罄
    public static final int PRODUCT_STATUS_PlAN = 2;//收益计划

    /*****投资状态*********/
    public static final int INVEST_STATUS_SUCCESS = 1;//成功
    public static final int INVEST_STATUS_FAIL = 2;//失败

    /*****收益状态*********/
    public static final int INCOME_STATUS_PLAN = 0;//生成收益计划
    public static final int INCOME_STATUS_BACK = 1;//收益返还

    /*****充值状态*********/
    public static final int RECHARGE_STATUS_PROCESSING = 0;//充值中
    public static final int RECHARGE_STATUS_SUCCESS = 1;//成功
    public static final int RECHARGE_STATUS_FAIL = 2;//失败

}
