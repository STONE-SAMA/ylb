package com.demo.front.controller;

import com.demo.api.model.User;
import com.demo.api.pojo.UserAccountInfo;
import com.demo.front.service.SmsService;
import com.demo.front.service.impl.RealnameServiceImpl;
import com.demo.front.view.RespResult;
import com.demo.front.vo.RealnameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.example.common.constants.Constants;
import org.example.common.util.CommonUtil;
import org.example.common.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    @Resource(name = "smsCodeRegisterImpl")
    private SmsService smsService;

    @Resource(name = "smsCodeLoginImpl")
    private SmsService loginSmsService;

    @Resource
    private RealnameServiceImpl realnameService;

    @Resource
    private JwtUtil jwtUtil;


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


    /**
     * 手机号注册用户
     *
     * @param phone
     * @param pword
     * @param scode
     * @return
     */
    @ApiOperation(value = "手机号注册用户")
    @PostMapping("/register")
    public RespResult userRegister(@RequestParam String phone,
                                   @RequestParam String pword,
                                   @RequestParam String scode) {
        RespResult result = RespResult.fail();
        //检查参数
        if (CommonUtil.checkPhone(phone)) {
            if (pword != null && pword.length() == 32) {
                //检查短信验证码
                if (smsService.checkSmsCode(phone, scode)) {
                    //可以注册
                    int res = userService.userRegister(phone, pword);
                    if (res == 1) {
                        result = RespResult.ok();
                    } else if (res == 2) {
                        result.setMsg("手机号已注册");
                    } else {
                        result.setMsg("参数有误");
                    }
                } else {
                    result.setMsg("验证码错误");
                }
            } else {
                result.setMsg("参数错误");
            }
        } else {
            //手机号格式错误
            result.setMsg("手机号格式错误");
        }
        return result;
    }


    /**
     * 登录，获取token-jwt
     *
     * @param phone
     * @param pword
     * @param scode
     * @return
     */
    @ApiOperation(value = "用户登录获取token")
    @PostMapping("/login")
    public RespResult userLogin(@RequestParam String phone,
                                @RequestParam String pword,
                                @RequestParam String scode) throws Exception {
        RespResult result = RespResult.fail();
        if (loginSmsService.checkSmsCode(phone, scode)) {
            User user = userService.userLogin(phone, pword);
            if (user != null) {
                //登录成功，生成token
                Map<String, Object> data = new HashMap<>();
                data.put("uid", user.getId());
                String jwtToken = jwtUtil.createJwt(data, 120);

                result = RespResult.ok();
                result.setMsg("登录成功！");
                result.setAccessToken(jwtToken);

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("uid", user.getId());
                userInfo.put("phone", user.getPhone());
                userInfo.put("name", user.getName());
                result.setData(userInfo);
            } else {
                result.setMsg("登录失败！");
            }
        }
        return result;
    }


    /**
     * 实名认证
     *
     * @return
     */
    @ApiOperation(value = "实名认证", notes = "提供手机号、姓名、身份证号，进行实名认证")
    @PostMapping("/realname")
    public RespResult userRealname(@RequestBody RealnameVO realnameVO) {
        RespResult result = RespResult.fail();
        result.setMsg("参数有误");
        //验证请求参数
        if (StringUtils.isNotBlank(realnameVO.getPhone()) &&
                StringUtils.isNotBlank(realnameVO.getIdCard())) {
            //判断用户是否已实名
            User user = userService.queryByPhone(realnameVO.getPhone());
            if (user != null) {
                if (StringUtils.isNotBlank(user.getName())) {
                    result.setMsg("已实名！");
                } else {
                    //TODO 发送短信验证码

                    //调用三方接口
                    boolean realnameRes = realnameService.handleRealname(realnameVO.getPhone(), realnameVO.getName(), realnameVO.getIdCard());
                    if (realnameRes) {
                        result = RespResult.ok();
                        result.setMsg("身份认证成功");
                    } else {
                        result.setMsg("身份认证失败");
                    }
                }
            }

        }
        return result;
    }


    /**
     * 用户中心
     *
     * @return
     */
    @ApiOperation(value = "用户中心")
    @GetMapping("/usercenter")
    public RespResult userCenter(@RequestHeader(value = "uid", required = false) Integer uid) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            UserAccountInfo userAccountInfo = userService.queryUserAllInfo(uid);
            if (userAccountInfo != null) {
                result = RespResult.ok();

                Map<String, Object> data = new HashMap<>();
                data.put("name", userAccountInfo.getName());
                data.put("phone", userAccountInfo.getPhone());
                data.put("headerUrl", userAccountInfo.getHeaderImage());
                data.put("money",userAccountInfo.getAvailableMoney());
                if (userAccountInfo.getLastLoginTime() != null) {
                    data.put("loginTime", DateFormatUtils.format(
                            userAccountInfo.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"
                    ));
                } else {
                    data.put("loginTime", null);
                }
                result.setData(data);
            }
        }

        return result;
    }

}
