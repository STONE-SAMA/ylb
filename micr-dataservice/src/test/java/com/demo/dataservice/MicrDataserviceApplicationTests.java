package com.demo.dataservice;

import com.demo.api.service.UserService;
import com.demo.dataservice.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MicrDataserviceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private UserService userService;

    @Test
    void testRegister(){
        String phone = "19157878845";
        String password = "a906449d5769fa7361d7ecc6aa3f6d28";

        int res = userService.userRegister(phone, password);
        System.out.println(res);

    }

}
