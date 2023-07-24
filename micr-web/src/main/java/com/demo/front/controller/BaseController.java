package com.demo.front.controller;

import com.demo.api.service.InvestService;
import com.demo.api.service.PlatBaseInfoService;
import com.demo.api.service.ProductService;
import com.demo.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class BaseController {

    //声明公共方法等

    @Resource
    protected StringRedisTemplate stringRedisTemplate;


    //平台信息服务
    @DubboReference(interfaceClass = PlatBaseInfoService.class, version = "1.0")
    protected PlatBaseInfoService platBaseInfoService;

    //产品服务
    @DubboReference(interfaceClass = ProductService.class, version = "1.0")
    protected ProductService productService;

    //投资服务
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;

    //用户服务
    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;

}
