package com.demo.api.service;

import com.demo.api.pojo.BidInfoProduct;

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

}
