package com.demo.front.controller;

import com.demo.api.model.User;
import com.demo.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    /**
     * 手机号是否存在
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "手机号是否已注册", notes = "判断手机号是否可以注册")
    @ApiImplicitParam(name = "phone", value = "手机号")
    @GetMapping("/phone/exists")
    public RespResult phoneExists(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        //1.检查请求参数是否符号要求
        if (CommonUtil.checkPhone(phone)) {
            //调用数据服务
            User user = userService.queryByPhone(phone);
            if (user == null) {
                result = RespResult.ok();
            } else {
                result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                result.setMsg("手机号已注册！");
            }
        } else {
            result.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            result.setMsg("手机号格式错误！");
        }
        return result;
    }

}
