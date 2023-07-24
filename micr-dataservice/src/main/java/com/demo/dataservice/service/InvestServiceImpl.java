package com.demo.dataservice.service;

import com.demo.api.pojo.BidInfoProduct;
import com.demo.api.service.InvestService;
import com.demo.dataservice.mapper.BidInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.util.CommonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl implements InvestService {

    @Resource
    private BidInfoMapper bidInfoMapper;

    /**
     * 查询某个产品的投资记录
     *
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize) {

        List<BidInfoProduct> bidList = new ArrayList<>();
        if (productId != null && productId > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageNo(pageSize);
            int offset = (pageNo - 1) * pageSize;
            bidList = bidInfoMapper.selectByProductId(productId, offset, pageSize);
        }

        return bidList;
    }
}
