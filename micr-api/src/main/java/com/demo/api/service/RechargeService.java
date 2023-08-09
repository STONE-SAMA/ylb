package com.demo.api.service;

import com.demo.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {

    /**
     * 根据userid查询充值记录
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize);

    int addRechargeRecord(RechargeRecord record);

    //处理后续充值
    int handleKQNotify(String orderId, String payAmount, String payResult);
}
