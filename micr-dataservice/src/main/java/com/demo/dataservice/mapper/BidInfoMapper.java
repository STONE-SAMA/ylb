package com.demo.dataservice.mapper;

import com.demo.api.model.BidInfo;

import java.math.BigDecimal;

public interface BidInfoMapper {

    /**
     * 累计交易金额
     * @return
     */
    BigDecimal selectSumBidMoney();


    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);
}
