package com.demo.api.service;

import com.demo.api.pojo.BaseInfo;

public interface PlatBaseInfoService {
    /**
     * 计算利率
     * 注册人数
     * 累计成交额
     */
    BaseInfo queryPlatBaseInfo();
}
