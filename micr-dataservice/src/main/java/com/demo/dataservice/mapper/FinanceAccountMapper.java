package com.demo.dataservice.mapper;

import com.demo.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    //给uid的记录上锁
    FinanceAccount selectByUidForUpdate(@Param("uid") Integer uid);

    //更新资金
    int updateAvailableMonryByInvest(@Param("uid") Integer uid, @Param("money") BigDecimal money);
}
