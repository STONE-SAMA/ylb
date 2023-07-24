package com.demo.api.service;

import com.demo.api.model.ProductInfo;
import com.demo.api.pojo.MultiProduct;

import java.util.List;

public interface ProductService {
    /**
     * 根据产品类型，查询产品，支持分页
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize);

    /**
     * 首页的多个产品数据
     *
     * @return
     */
    MultiProduct queryIndexPageProducts();

    /**
     * 某个产品的记录总数
     * @param pType
     * @return
     */
    Integer queryRecordNumsByType(Integer pType);

    /**
     * 根据产品id，查询产品信息
     * @param id
     * @return
     */
    ProductInfo queryById(Integer id);

}
