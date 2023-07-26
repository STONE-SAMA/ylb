package com.demo.front.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demo.front.view.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.example.common.constants.Constants;
import org.example.common.util.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    private String secret = "";

    public TokenInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果是OPTIONS，放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        boolean requestSend = false;
        try {
            //获取token的值，进行验证
            String headerToken = request.getHeader("Authorization");
            String headerUid = request.getHeader("uid");
            if (StringUtils.isNotBlank(headerToken)) {
                String jwt = headerToken.substring(7);
                //读jwt
                JwtUtil jwtUtil = new JwtUtil(secret);
                Claims claims = jwtUtil.readJwt(jwt);
                //获取jwt中的数据，uid
                Integer jwtUid = claims.get("uid", Integer.class);
                if (headerUid.equals(String.valueOf(jwtUid))) {
                    //token和发起请求用户是同一个。 请求可以被处理
                    requestSend = true;
                }
            }
        } catch (Exception e) {
            requestSend = false;
            e.printStackTrace();
        }

        if (requestSend == false) {
            //返回json数据给前端
            RespResult result = RespResult.fail();
            result.setCode(Constants.RETURN_OBJECT_CODE_TOKEN_FAIL);
            //使用HttpServletResponse输出 json
            String respJson = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(respJson);
            out.flush();
            out.close();
        }
        return requestSend;
    }
}
