package com.demo.api.service;

import com.demo.api.model.User;
import com.demo.api.pojo.UserAccountInfo;

public interface UserService {

    //根据手机号查询数据
    User queryByPhone(String phone);

    //用户注册
    int userRegister(String phone, String password);

    //登录
    User userLogin(String phone, String pword);

    //更新实名认证信息
    boolean modifyRealname(String phone, String name, String idCard);

    //获取用户和资金信息
    UserAccountInfo queryUserAllInfo(Integer uid);

    //查询用户
    User queryById(Integer uid);
}
