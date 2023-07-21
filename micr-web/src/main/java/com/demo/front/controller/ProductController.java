package com.demo.front.controller;

import com.demo.api.model.ProductInfo;
import com.demo.api.pojo.MultiProduct;
import com.demo.front.view.PageInfo;
import com.demo.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController {

    @ApiOperation(value = "首页三类产品列表", notes = "新手宝，三个优选，三个散标商品")
    @GetMapping("/product/index")
    public RespResult queryProductIndex() {
        RespResult result = new RespResult();
        MultiProduct multiProduct = productService.queryIndexPageProducts();
        result.setData(multiProduct);
        return result;
    }

    //按产品类型分页查询
    @ApiOperation(value = "")
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam("pType") Integer pType,
                                         @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (pType != null && (pType == 0 || pType == 1 || pType == 2)) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            //分页处理总记录数
            Integer recordNums = productService.queryRecordNumsByType(pType);
            if (recordNums > 0) {
                //产品集合
                List<ProductInfo> productInfos = productService.queryByTypeLimit(pType, pageNo, pageSize);
                //构建pageInfo
                PageInfo page = new PageInfo(pageNo, pageSize, recordNums);

                result = RespResult.ok();
                result.setList(productInfos);
                result.setPage(page);
            }else {
                result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                result.setMsg("产品数量为空");
            }
        } else {
            //产品类型有误
            result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            result.setMsg("请求产品类型有误");
        }
        return result;
    }

}
