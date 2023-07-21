package com.demo.dataservice.service;

import com.demo.api.model.ProductInfo;
import com.demo.api.pojo.MultiProduct;
import com.demo.api.service.ProductService;
import com.demo.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class, version = "1.0")
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {
        List<ProductInfo> productInfos = new ArrayList<>();
        if (pType == 0 || pType == 1 || pType == 2) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            productInfos = productInfoMapper.selectByTypeLimit(pType, offset, pageSize);
        }
        return productInfos;
    }

    @Override
    public MultiProduct queryIndexPageProducts() {
        MultiProduct result = new MultiProduct();
        //查询新手宝
        List<ProductInfo> list_xinshoubao = productInfoMapper.selectByTypeLimit(
                Constants.PRODUCT_TYPE_XINSHOUBAO, 0, 1);
        //查询优选
        List<ProductInfo> list_youxuan = productInfoMapper.selectByTypeLimit(
                Constants.PRODUCT_TYPE_YOUXUAN, 0, 3);
        //查询散标
        List<ProductInfo> list_sanbiao = productInfoMapper.selectByTypeLimit(
                Constants.PRODUCT_TYPE_SANBIAO, 0, 3);
        result.setXinShouBao(list_xinshoubao);
        result.setYouXuan(list_youxuan);
        result.setSanBiao(list_sanbiao);
        return result;
    }

    @Override
    public Integer queryRecordNumsByType(Integer pType) {
        Integer counts = 0;
        if (pType == 0 || pType == 1 || pType == 2) {
            counts = productInfoMapper.selectCountByType(pType);
        }
        return counts;
    }
}
