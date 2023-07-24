package com.demo.dataservice.service;

import com.demo.api.model.User;
import com.demo.api.service.UserService;
import com.demo.dataservice.mapper.UserMapper;
import org.example.common.util.CommonUtil;

import javax.annotation.Resource;

public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据手机号查询数据
     *
     * @param phone
     * @return
     */
    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)) {
            user = userMapper.selectByPhone(phone);
        }
        return user;
    }
}
