package com.demo.dataservice.mapper;

import com.demo.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductInfoMapper {

    //平均利率
    BigDecimal selectAvgRate();

    //根据产品类型，查询产品，支持分页
    List<ProductInfo> selectByTypeLimit(@Param("pType") Integer pType,
                                        @Param("offset") Integer offset,
                                        @Param("rows") Integer rows);

    //某类型产品的记录总数
    Integer selectCountByType(@Param("pType") Integer pType);



    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);
}
