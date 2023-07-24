package com.demo.dataservice.mapper;

import com.demo.api.model.BidInfo;
import com.demo.api.pojo.BidInfoProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {

    /**
     * 累计交易金额
     *
     * @return
     */
    BigDecimal selectSumBidMoney();

    /**
     * 某个产品投资记录
     *
     * @param productId
     * @param offset
     * @param rows
     * @return
     */
    List<BidInfoProduct> selectByProductId(@Param("productId") Integer productId,
                                           @Param("offset") int offset,
                                           @Param("rows") Integer rows);

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


}
