package com.demo.api.service;

import com.demo.api.pojo.BidInfoProduct;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {

    /**
     * 查询某个产品的投资记录
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<BidInfoProduct> queryBidListByProductId(Integer productId,
                                                 Integer pageNo,
                                                 Integer pageSize);

    /**
     * 购买理财产品
     * @param uid
     * @param productId
     * @param money
     * @return 1表示成功
     */
    int investProduct(Integer uid, Integer productId, BigDecimal money);
}
