package com.demo.dataservice.service;

import com.demo.api.model.RechargeRecord;
import com.demo.api.service.RechargeService;
import com.demo.dataservice.mapper.FinanceAccountMapper;
import com.demo.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@DubboService(interfaceClass = RechargeService.class, version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    @Resource
    private FinanceAccountMapper accountMapper;

    /**
     * 根据userid查询充值记录
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = rechargeRecordMapper.selectByUid(uid, offset, pageSize);
        }
        return records;
    }

    @Override
    public int addRechargeRecord(RechargeRecord record) {
        return rechargeRecordMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0;//订单不存在
        int rows = 0;
        //查询订单
        RechargeRecord record = rechargeRecordMapper.selectByRechargeNo(orderId);
        if (record != null) {
            if (record.getRechargeStatus() == Constants.RECHARGE_STATUS_PROCESSING) {
                //判断金额是否一致
                String fen = record.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if (fen.equals(payAmount)) {
                    if ("10".equals(payResult)) {
                        //成功
                        rows = accountMapper.updateAvailableMoneyByRecharge(record.getUid(), record.getRechargeMoney());
                        if (rows < 1) {
                            throw new RuntimeException("充值更新资金账户失败");
                        }
                        //更新充值记录的状态
                        rows = rechargeRecordMapper.updateStatus(record.getId(), Constants.RECHARGE_STATUS_SUCCESS);
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 1;
                    } else {
                        //充值失败
                        rows = rechargeRecordMapper.updateStatus(record.getId(), Constants.RECHARGE_STATUS_FAIL);
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 2;
                    }
                } else {
                    result = 4;//金额不一致
                }
            } else {
                result = 3;//订单已处理
            }
        }
        return result;
    }
}
