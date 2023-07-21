package com.demo.dataservice.service;

import com.demo.api.pojo.BaseInfo;
import com.demo.api.service.PlatBaseInfoService;
import com.demo.dataservice.mapper.BidInfoMapper;
import com.demo.dataservice.mapper.ProductInfoMapper;
import com.demo.dataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;

@DubboService(interfaceClass = PlatBaseInfoService.class, version = "1.0")
public class PlatBaseInfoServiceImpl implements PlatBaseInfoService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    /**
     * 平台基本信息
     * @return
     */
    @Override
    public BaseInfo queryPlatBaseInfo() {
        //获取注册人数，收益率平均值，累计成交额
        int countUser = userMapper.selectCountUser();
        BigDecimal avgRate = productInfoMapper.selectAvgRate();
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();

        BaseInfo baseInfo = new BaseInfo(avgRate, sumBidMoney, countUser);
        return baseInfo;
    }
}
