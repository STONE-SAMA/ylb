package com.example.pay.Controller;

import com.demo.api.model.User;
import com.example.pay.service.KuaiQianService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Component
@RequestMapping("kq")
public class KuaiQianController {

    @Resource
    private KuaiQianService kqService;

    //接收来自vue的支付充值请求
    @GetMapping("/rece/recharge")
    public String fromFrontRechargeKQ(Integer uid, BigDecimal rechargeMoney, Model model) {
        String view = "err";//默认错误视图
        if (uid != null && uid > 0 && rechargeMoney != null && rechargeMoney.doubleValue() > 0) {

            //检查uid是否是有效用户
            try {
                User user = kqService.queryUser(uid);
                if (user != null) {
                    //创建快钱支付接口所需参数
                    Map<String, String> data = kqService.generateFormData(uid, user.getPhone(), rechargeMoney);
                    model.addAttribute(data);
                    //创建充值记录
                    kqService.addRecharge(uid,rechargeMoney,data.get("orderId"));
                    //把订单号存放到redis
                    kqService.addOrderIdToRedis(data.get("orderId"));
                    //提交支付请求给快钱的form页面
                    view = "kqForm";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return view;
    }

    //接收快钱给商家的支付结果 , 快钱以get方式，发送请求给商家
    @GetMapping("/rece/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request){
        System.out.println("=================接收快钱的异步通知=============");
        kqService.kqNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    //从定时任务调用接口
    @GetMapping("/rece/query")
    @ResponseBody
    public String queryKQOrder(){
        kqService.handleQueryOrder();
        return "接收了查询的请求";
    }


}
