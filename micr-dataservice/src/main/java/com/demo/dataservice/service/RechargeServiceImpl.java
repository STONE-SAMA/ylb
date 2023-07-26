package com.demo.dataservice.service;

import com.demo.api.model.RechargeRecord;
import com.demo.api.service.RechargeService;
import com.demo.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.util.CommonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@DubboService(interfaceClass = RechargeService.class, version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

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
}
