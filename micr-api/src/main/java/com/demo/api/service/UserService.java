package com.demo.api.service;

import com.demo.api.model.User;

public interface UserService {

    //根据手机号查询数据
    User queryByPhone(String phone);

    //用户注册
    int userRegister(String phone, String password);

    //登录
    User userLogin(String phone, String pword);

    //更新实名认证信息
    boolean modifyRealname(String phone, String name, String idCard);
}
