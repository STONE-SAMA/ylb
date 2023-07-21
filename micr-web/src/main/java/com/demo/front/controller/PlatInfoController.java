package com.demo.front.controller;

import com.demo.api.pojo.BaseInfo;
import com.demo.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.constants.Constants;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Api(tags = "平台信息功能")
@RestController
@RequestMapping("/v1")
public class PlatInfoController extends BaseController{

    /**
     * 平台基本信息
     */
    @ApiOperation(value = "平台基本信息", notes = "注册人数、平均利率、总分投资金额")
    @GetMapping("/plat/info")
    public RespResult queryPlatBaseInfo(){
        BaseInfo baseInfo = platBaseInfoService.queryPlatBaseInfo();

        RespResult result = new RespResult();
        result.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        result.setMsg("查询平台信息成功");
        result.setData(baseInfo);
        return result;
    }
}
