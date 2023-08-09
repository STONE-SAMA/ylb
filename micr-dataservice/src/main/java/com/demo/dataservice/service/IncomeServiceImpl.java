package com.demo.dataservice.service;

import com.demo.api.model.BidInfo;
import com.demo.api.model.IncomeRecord;
import com.demo.api.model.ProductInfo;
import com.demo.api.service.IncomeService;
import com.demo.dataservice.mapper.BidInfoMapper;
import com.demo.dataservice.mapper.FinanceAccountMapper;
import com.demo.dataservice.mapper.IncomeRecordMapper;
import com.demo.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.constants.Constants;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class, version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private IncomeRecordMapper incomeMapper;

    @Resource
    private FinanceAccountMapper accountMapper;

    /**
     * 收益计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void generateIncomePlan() {
        //获取待处理的理财产品记录
        Date currentDate = new Date();
        Date beginTime = DateUtils.truncate(DateUtils.addDays(currentDate, -1), Calendar.DATE);
        Date endTime = DateUtils.truncate(currentDate, Calendar.DATE);
        List<ProductInfo> productInfoList = productInfoMapper.selectFullTimeProducts(beginTime, endTime);
        //查询每个理财产品的多个投资记录
        int rows = 0;

        BigDecimal income = null;
        BigDecimal dayRate = null;
        BigDecimal cycle = null; //周期

        Date incomeDate = null;//到期时间

        for (ProductInfo product : productInfoList) {

            //产品类型不同，周期不同 天 ，月
            if (product.getProductType() == Constants.PRODUCT_TYPE_XINSHOUBAO) {  //天为单位
                cycle = new BigDecimal(product.getCycle());
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle()));
            } else {
                cycle = new BigDecimal(product.getCycle() * 30);
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle() * 30));
            }

            List<BidInfo> bidList = bidInfoMapper.selectByProdId(product.getId());

            //计算每笔投资的 利息 和 到期时间
            for (BidInfo bid : bidList) {
                //利息 = 本金  * 周期 * 利率
                income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
                // 创建收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bid.getId());
                incomeRecord.setBidMoney(bid.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeStatus(Constants.INCOME_STATUS_PLAN);
                incomeRecord.setProdId(product.getId());
                incomeRecord.setIncomeMoney(income);
                incomeRecord.setUid(bid.getUid());
                incomeMapper.insertSelective(incomeRecord);
            }
            //更新产品的状态
            rows = productInfoMapper.updateStatus(product.getId(), Constants.INCOME_STATUS_PLAN);
            if (rows < 1) {
                throw new RuntimeException("生成收益计划，更新产品状态为2失败");
            }

        }


    }

    /**
     * 收益返还
     */
    @Override
    public void generateIncomeBack() {
        //获取要处理的到期收益记录
        Date curDate = new Date();
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(curDate, -1), Calendar.DATE);
        System.out.println("expiredDate=" + expiredDate);
        List<IncomeRecord> incomeRecordList = incomeMapper.selectExpiredIncome(expiredDate);

        int rows = 0;
        //把每个收益，进行返还， 本金 + 利息
        for (IncomeRecord incomeRecord : incomeRecordList) {
            rows = accountMapper.updateAvailableMoneyByIncomeBack(
                    incomeRecord.getUid(),
                    incomeRecord.getBidMoney(),
                    incomeRecord.getIncomeMoney());
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新账号资金失败");
            }
            //更新收益记录的状态为 1
            incomeRecord.setIncomeStatus(Constants.INCOME_STATUS_BACK);
            rows = incomeMapper.updateByPrimaryKey(incomeRecord);
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新收益记录的状态失败");
            }

        }

    }
}
