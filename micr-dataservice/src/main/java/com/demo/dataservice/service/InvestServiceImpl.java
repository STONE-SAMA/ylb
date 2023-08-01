package com.demo.dataservice.service;

import com.demo.api.model.BidInfo;
import com.demo.api.model.FinanceAccount;
import com.demo.api.model.ProductInfo;
import com.demo.api.pojo.BidInfoProduct;
import com.demo.api.service.InvestService;
import com.demo.dataservice.mapper.BidInfoMapper;
import com.demo.dataservice.mapper.FinanceAccountMapper;
import com.demo.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl implements InvestService {

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Resource
    private ProductInfoMapper productInfoMapper;

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

    /**
     * 购买理财产品
     *
     * @param uid
     * @param productId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int investProduct(Integer uid, Integer productId, BigDecimal money) {
        int result = 0;//默认，参数不正确
        int rows = 0;
        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                && (money != null && money.intValue() % 100 == 0 && money.intValue() >= 100)) {
            //查询账户金额
            FinanceAccount account = financeAccountMapper.selectByUidForUpdate(uid);
            if (account != null) {
                if (CommonUtil.ge(account.getAvailableMoney(), money)) {
                    //资金满足要求
                    //检查产品是否可以购买
                    ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
                    if (productInfo != null && productInfo.getProductStatus() == Constants.PRODUCT_STATUS_SELLING) {
                        if (CommonUtil.ge(productInfo.getLeftProductMoney(), money) &&
                                CommonUtil.ge(money, productInfo.getBidMinLimit()) &&
                                CommonUtil.ge(productInfo.getBidMaxLimit(), money)) {
                            //购买，扣除账户资金
                            rows = financeAccountMapper.updateAvailableMonryByInvest(uid, money);
                            if (rows < 1) {
                                throw new RuntimeException("更新账户资金失败");
                            }
                            //扣除产品剩余可投资金额
                            rows = productInfoMapper.updateLeftProductMoney(productId, money);
                            if (rows < 1) {
                                throw new RuntimeException("更新产品剩余金额失败");
                            }
                            //创建投资记录
                            BidInfo bidInfo = new BidInfo();
                            bidInfo.setBidMoney(money);
                            bidInfo.setBidStatus(Constants.INVEST_STATUS_SUCCESS);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setProdId(productId);
                            bidInfo.setUid(uid);
                            bidInfoMapper.insertSelective(bidInfo);
                            //更新产品的状态。是否满标
                            ProductInfo dbproductInfo = productInfoMapper.selectByPrimaryKey(productId);
                            if (dbproductInfo.getLeftProductMoney().compareTo(new BigDecimal("0")) == 0) {
                                rows = productInfoMapper.updateSelled(productId, Constants.PRODUCT_STATUS_SOLDOUT);
                                if (rows < 1) {
                                    throw new RuntimeException("投资更新产品满标失败");
                                }
                            }
                            //投资成功
                            result = 1;
                        }
                    } else {
                        result = 4;//理财产品不存在
                    }
                } else {
                    //资金不足
                    result = 3;
                }
            } else {
                result = 2;//资金账户不存在
            }
        }
        return result;
    }
}
