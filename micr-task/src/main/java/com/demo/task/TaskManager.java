package com.demo.task;

import com.demo.api.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.util.HttpUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskManager {

    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    private IncomeService incomeService;

    /**
     * 生成收益计划
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan(){
        incomeService.generateIncomePlan();
    }

    /**
     * 收益返还
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomeBack(){
        incomeService.generateIncomeBack();
    }

    /**
     * 补单接口
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void invokeKuaiQianQuery(){
        try{
            String url = "http://localhost:9000/pay/kq/rece/query";
            HttpUtils.doGet(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
